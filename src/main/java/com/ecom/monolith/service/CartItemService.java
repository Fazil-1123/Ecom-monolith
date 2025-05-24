package com.ecom.monolith.service;

import com.ecom.monolith.Dto.CartRequest;

public interface CartItemService {
    Boolean addCartItem(String userId, CartRequest cartRequest);
}
