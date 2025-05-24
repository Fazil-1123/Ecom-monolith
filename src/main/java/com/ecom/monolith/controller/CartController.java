package com.ecom.monolith.controller;

import com.ecom.monolith.Dto.CartRequest;
import com.ecom.monolith.service.CartItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartItemService cartItemService;

    public CartController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<String> addItem(@RequestHeader("X-User-ID")String userId, @RequestBody CartRequest cartRequest){

        if(!cartItemService.addCartItem(userId, cartRequest)){
            return new ResponseEntity<>("Out of stock!!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Added to cart",HttpStatus.OK);
    }
}
