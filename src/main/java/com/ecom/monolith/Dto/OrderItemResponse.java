package com.ecom.monolith.Dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponse {

    private Long id;

    private Long productId;

    private Integer quantity;

    private BigDecimal price;
}
