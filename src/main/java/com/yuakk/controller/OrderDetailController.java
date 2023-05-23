package com.yuakk.controller;

import com.yuakk.service.OrderDetailService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yuakk
 */
@RestController
@RequestMapping("/orderDetail")
public class OrderDetailController {

    @Resource
    private OrderDetailService orderDetailService;


}
