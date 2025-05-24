package com.ecom.monolith.service;

import com.ecom.monolith.Dto.CartRequest;
import com.ecom.monolith.Dto.CartResponse;
import com.ecom.monolith.Mapper.CartMapper;
import com.ecom.monolith.exception.ResourceNotFound;
import com.ecom.monolith.model.CartItem;
import com.ecom.monolith.model.Product;
import com.ecom.monolith.model.Users;
import com.ecom.monolith.repositories.CartItemRepository;
import com.ecom.monolith.repositories.ProductRepository;
import com.ecom.monolith.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private final UsersRepository usersRepository;

    private final ProductRepository productRepository;

    private final CartItemRepository cartItemRepository;

    private final CartMapper cartMapper;

    public CartItemServiceImpl(UsersRepository usersRepository, ProductRepository productRepository, CartItemRepository cartItemRepository, CartMapper cartMapper) {
        this.usersRepository = usersRepository;
        this.productRepository = productRepository;
        this.cartItemRepository = cartItemRepository;
        this.cartMapper = cartMapper;
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

    @Override
    public boolean removeItem(String userId, Long productId) {
        Product product =
                productRepository.findById(productId).orElseThrow(
                        ()-> new ResourceNotFound("Product not found with id: "+productId)
                );
        Users users = usersRepository.findById(Long.valueOf(userId)).
                orElseThrow(()->new ResourceNotFound("User does not exist with id: "+userId));
        Optional<CartItem> cartItem = cartItemRepository.findByUsersAndProduct(users,product);
        if(cartItem.isPresent()){
            cartItemRepository.delete(cartItem.get());
            return true;
        }

        return false;
    }

    @Override
    public List<CartResponse> getCartItems(String userId) {
        usersRepository.findById(Long.valueOf(userId)).orElseThrow(
                ()->new ResourceNotFound("User does not exist with id: "+userId)
        );
        return cartItemRepository.findByUsersId(Long.valueOf(userId))
                .stream().map(cartMapper::toDto).toList();
    }
}
