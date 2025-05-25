package com.ecom.monolith.controller;

import com.ecom.monolith.Dto.OrderResponse;
import com.ecom.monolith.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestHeader("X-User-ID")String userId){
        OrderResponse orderResponse = orderService.placeOrder(userId);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
