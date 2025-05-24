package com.ecom.monolith.controller;

import com.ecom.monolith.Dto.CartRequest;
import com.ecom.monolith.Dto.CartResponse;
import com.ecom.monolith.service.CartItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartItemService cartItemService;

    public CartController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping
    public ResponseEntity<String> addItem(@RequestHeader("X-User-ID")String userId,@Valid @RequestBody CartRequest cartRequest){

        if(!cartItemService.addCartItem(userId, cartRequest)){
            return new ResponseEntity<>("Out of stock!!", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Added to cart",HttpStatus.OK);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<String> removeItem(@RequestHeader("X-User-ID")String userId,
                                             @PathVariable("productId") Long productId ){
        if(!cartItemService.removeItem(userId, productId)){
            return new ResponseEntity<>("Cart doesnt exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Removed from cart",HttpStatus.OK);
    }

    @GetMapping
    public List<CartResponse> getCartItems(@RequestHeader("X-User-ID")String userId){
        return cartItemService.getCartItems(userId);
    }

}
