package com.ecom.monolith.service;


import com.ecom.monolith.Dto.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(String userId);

}
