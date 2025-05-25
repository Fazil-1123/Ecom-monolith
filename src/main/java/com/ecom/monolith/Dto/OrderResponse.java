package com.ecom.monolith.Dto;

import com.ecom.monolith.model.OrderItem;
import com.ecom.monolith.model.OrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderResponse {

    private Long id;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private List<OrderItemResponse> items = new ArrayList<>();
}
