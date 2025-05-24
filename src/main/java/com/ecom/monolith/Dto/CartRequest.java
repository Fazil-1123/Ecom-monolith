package com.ecom.monolith.Dto;

import lombok.Data;

@Data
public class CartRequest {

    private String productId;

    private int quantity;
}
