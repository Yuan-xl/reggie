package com.yuakk.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yuakk.dto.OrderDto;
import com.yuakk.pojo.Orders;

/**
 * @author yuakk
 */
public interface OrdersService extends IService<Orders> {
    /**
     * 提交订单
     * @param orders Orders
     * @return boolean
     */
    boolean submit(Orders orders);

    /**
     * 分页查询及详情
     * @param page page
     * @param pageSize pageSize
     * @return Page<OrderDto>
     */
    Page<OrderDto> getOrderByPageAndDetails(int page,int pageSize);
}
