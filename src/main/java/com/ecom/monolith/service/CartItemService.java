package com.ecom.monolith.service;

import com.ecom.monolith.Dto.CartRequest;
import com.ecom.monolith.Dto.CartResponse;

import java.util.List;

public interface CartItemService {
    Boolean addCartItem(String userId, CartRequest cartRequest);

    boolean removeItem(String userId, Long productId);

    List<CartResponse> getCartItems(String userId);
}
