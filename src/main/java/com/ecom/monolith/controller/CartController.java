package com.ecom.monolith.controller;

import com.ecom.monolith.Dto.CartRequest;
import com.ecom.monolith.Dto.CartResponse;
import com.ecom.monolith.service.CartItemService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private static final Logger logger = LoggerFactory.getLogger(CartController.class);
    private final CartItemService cartItemService;

    public CartController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<String> addItem(@RequestHeader("X-User-ID") String userId,
                                          @Valid @RequestBody CartRequest cartRequest) {
        logger.info("POST /api/cart - Adding item to cart for userId={}, productId={}", userId, cartRequest.getProductId());

        if (!cartItemService.addCartItem(userId, cartRequest)) {
            logger.warn("Out of stock for productId={} requested by userId={}", cartRequest.getProductId(), userId);
            return new ResponseEntity<>("Out of stock!!", HttpStatus.NOT_FOUND);
        }

        logger.info("Item added to cart successfully for userId={}", userId);
        return new ResponseEntity<>("Added to cart", HttpStatus.OK);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeItem(@RequestHeader("X-User-ID") String userId,
                                             @PathVariable("productId") Long productId) {
        logger.info("DELETE /api/cart/items/{} - Removing item for userId={}", productId, userId);

        if (!cartItemService.removeItem(userId, productId)) {
            logger.warn("Cart not found or item not present for userId={}, productId={}", userId, productId);
            return new ResponseEntity<>("Cart doesnt exist", HttpStatus.NOT_FOUND);
        }

        logger.info("Item removed from cart successfully for userId={}, productId={}", userId, productId);
        return new ResponseEntity<>("Removed from cart", HttpStatus.OK);
    }

    @GetMapping
    public List<CartResponse> getCartItems(@RequestHeader("X-User-ID") String userId) {
        logger.info("GET /api/cart - Fetching cart items for userId={}", userId);
        return cartItemService.getCartItems(userId);
    }
}
