package com.yuakk.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yuakk.dto.OrderDto;
import com.yuakk.pojo.Orders;
import com.yuakk.service.OrdersService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author yuakk
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrdersController {

    @Resource
    private OrdersService ordersService;

    @PostMapping("/submit")
    public boolean submit(@RequestBody Orders orders){
        log.info("orders:{}",orders.toString());
        return ordersService.submit(orders);
    }

    @GetMapping("/userPage")
    public Page<OrderDto> getUserOrderByPage(@RequestParam int page, @RequestParam int pageSize){
        return ordersService.getOrderByPageAndDetails(page,pageSize);
    }

    @GetMapping("/page")
    public Page<Orders> getOrdersByPage(@RequestParam int page, @RequestParam int pageSize,
                                        @RequestParam(value = "number",required = false) Long number,
                                        @RequestParam(value = "beginTime",required = false) String beginTime,
                                        @RequestParam(value = "endTime",required = false) String endTime){
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime)){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime parseBeginTime = LocalDateTime.parse(beginTime, formatter);
            LocalDateTime parseEndTime = LocalDateTime.parse(endTime, formatter);
            lambdaQueryWrapper.between(StringUtils.isNotBlank(beginTime) && StringUtils.isNotBlank(endTime),
                    Orders::getOrderTime, parseBeginTime, parseEndTime);
        }
        lambdaQueryWrapper.eq(number != null, Orders::getNumber,number);
        Page<Orders> pageInfo = new Page<>(page,pageSize);
        return ordersService.page(pageInfo,lambdaQueryWrapper);
    }

    @PutMapping
    public boolean changeStatus(@RequestBody Orders orders){
        return ordersService.updateById(orders);
    }
}
