package com.ecom.monolith.controller;

import com.ecom.monolith.Dto.OrderResponse;
import com.ecom.monolith.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestHeader("X-User-ID") String userId) {
        logger.info("POST /api/orders - Placing order for userId={}", userId);

        OrderResponse orderResponse = orderService.placeOrder(userId);

        logger.info("Order placed successfully for userId={}, orderId={}", userId, orderResponse.getId());
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
    }
}
