package com.yuakk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yuakk.mapper.OrderDetailMapper;
import com.yuakk.pojo.OrderDetail;
import com.yuakk.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * @author yuakk
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
