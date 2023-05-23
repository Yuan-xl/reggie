package com.yuakk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuakk.common.BaseContext;
import com.yuakk.common.BizException;
import com.yuakk.common.ErrorCodeEnum;
import com.yuakk.dto.OrderDto;
import com.yuakk.mapper.AddressBookMapper;
import com.yuakk.mapper.OrdersMapper;
import com.yuakk.mapper.ShoppingCartMapper;
import com.yuakk.mapper.UserMapper;
import com.yuakk.pojo.*;
import com.yuakk.service.OrderDetailService;
import com.yuakk.service.OrdersService;
import com.yuakk.service.ShoppingCartService;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author yuakk
 */
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Resource
    private ShoppingCartMapper shoppingCartMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private AddressBookMapper addressBookMapper;

    @Resource
    private OrderDetailService orderDetailService;

    @Resource
    private ShoppingCartService shoppingCartService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submit(Orders orders) {
        Long userId = BaseContext.getCurrentId();
        //查询购物车数据
        LambdaQueryWrapper<ShoppingCart> shoppingCartWrapper = new LambdaQueryWrapper<>();
        shoppingCartWrapper.eq(ShoppingCart::getUserId,userId);
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectList(shoppingCartWrapper);
        if (shoppingCarts==null || shoppingCarts.size() == 0){
            throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0406);
        }
        //用户数据
        User user = userMapper.selectById(userId);
        //地址数据
        AddressBook addressBook = addressBookMapper.selectById(orders.getAddressBookId());
        if (addressBook == null){
            throw new BizException(400, ErrorCodeEnum.USER_ERROR_A0419);
        }

        //向订单表插入数据
        //订单号
        long id = IdWorker.getId();
        //金额
        AtomicInteger allAmount = new AtomicInteger(0);
        //订单明细
        List<OrderDetail> orderDetails = shoppingCarts.stream().map((item)->{
            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setOrderId(id);
            orderDetail.setNumber(item.getNumber());
            orderDetail.setDishFlavor(item.getDishFlavor());
            orderDetail.setDishId(item.getDishId());
            orderDetail.setSetmealId(item.getSetmealId());
            orderDetail.setName(item.getName());
            orderDetail.setImage(item.getImage());
            orderDetail.setAmount(item.getAmount());

            allAmount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());

            return orderDetail;
        }).collect(Collectors.toList());

        orders.setNumber(String.valueOf(id));
        orders.setId(id);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(String.valueOf(allAmount)));
        orders.setUserId(userId);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null?"":addressBook.getProvinceName()) +
                            (addressBook.getCityName()==null?"":addressBook.getCityName())+
                            (addressBook.getDistrictName()==null?"":addressBook.getDistrictName()) +
                            (addressBook.getDetail()==null?"":addressBook.getDetail()));
        boolean saveOrders = this.save(orders);
        //向订单明细表插入数据
        boolean saveOrderDetail = orderDetailService.saveBatch(orderDetails);
        //清空购物车数据
        boolean removeShoppingCart = shoppingCartService.remove(shoppingCartWrapper);
        return saveOrders && saveOrderDetail && removeShoppingCart;
    }

    @Override
    public Page<OrderDto> getOrderByPageAndDetails(int page, int pageSize) {
        Page<Orders> ordersPageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByDesc(Orders::getOrderTime);
        this.page(ordersPageInfo, lambdaQueryWrapper);

        Page<OrderDto> orderDtoPage = new Page<>();
        BeanUtils.copyProperties(ordersPageInfo, orderDtoPage,"records");

        List<Orders> ordersList = ordersPageInfo.getRecords();
        List<OrderDto> orderDtoList = ordersList.stream().map((item->{
            OrderDto orderDto = new OrderDto();
            BeanUtils.copyProperties(item, orderDto);

            LambdaQueryWrapper<OrderDetail> lambdaWrapper = new LambdaQueryWrapper<>();
            lambdaWrapper.eq(item.getId()!=null,OrderDetail::getOrderId,item.getId());
            List<OrderDetail> orderDetailList = orderDetailService.list(lambdaWrapper);
            orderDto.setOrderDetails(orderDetailList);
            return orderDto;
        })).collect(Collectors.toList());
        orderDtoPage.setRecords(orderDtoList);
        return orderDtoPage;
    }
}
