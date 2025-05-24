package com.ecom.monolith.service;

import com.ecom.monolith.Dto.CartRequest;
import com.ecom.monolith.exception.ResourceNotFound;
import com.ecom.monolith.model.CartItem;
import com.ecom.monolith.model.Product;
import com.ecom.monolith.model.Users;
import com.ecom.monolith.repositories.CartItemRepository;
import com.ecom.monolith.repositories.ProductRepository;
import com.ecom.monolith.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final UsersRepository usersRepository;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    public CartItemServiceImpl(UsersRepository usersRepository, ProductRepository productRepository, CartItemRepository cartItemRepository) {
        this.usersRepository = usersRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public Boolean addCartItem(String userId, CartRequest cartRequest) {

        Product product =
                productRepository.findById(Long.valueOf(cartRequest.getProductId())).orElseThrow(
                        ()-> new ResourceNotFound("Product not found with id: "+cartRequest.getProductId())
                );
        if(product.getStockQuantity()< cartRequest.getQuantity()){
            return false;
        }
        Users users =
                usersRepository.findById(Long.valueOf(userId)).orElseThrow(
                        ()-> new ResourceNotFound("User does not exist with id: "+userId)
                );

        Optional<CartItem> cartItem = cartItemRepository.findByUsersAndProduct(users,product);
        if (cartItem.isPresent()) {
            CartItem existingItem = cartItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartRequest.getQuantity());
            existingItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            cartItemRepository.save(existingItem);
            return true;
        }
        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setUsers(users);
        newCartItem.setQuantity(cartRequest.getQuantity());
        newCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartRequest.getQuantity())));
        cartItemRepository.save(newCartItem);

        return true;
    }
}
