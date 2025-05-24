package com.ecom.monolith.Dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartResponse {

    private String productId;

    private Integer quantity;

    private BigDecimal price;
}
