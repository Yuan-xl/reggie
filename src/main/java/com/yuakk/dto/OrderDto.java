package com.yuakk.dto;

import com.yuakk.pojo.OrderDetail;
import com.yuakk.pojo.Orders;
import lombok.Data;

import java.util.List;

/**
 * @author yuakk
 */
@Data
public class OrderDto extends Orders {
    private List<OrderDetail> orderDetails;
}
