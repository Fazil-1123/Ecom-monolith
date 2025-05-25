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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {

    private static final Logger logger = LoggerFactory.getLogger(CartItemServiceImpl.class);

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
        logger.info("Adding item to cart for userId={}, productId={}, quantity={}", userId, cartRequest.getProductId(), cartRequest.getQuantity());

        Product product = productRepository.findById(Long.valueOf(cartRequest.getProductId()))
                .orElseThrow(() -> new ResourceNotFound("Product not found with id: " + cartRequest.getProductId()));

        if (product.getStockQuantity() < cartRequest.getQuantity()) {
            logger.warn("Out of stock for productId={} requested by userId={}", cartRequest.getProductId(), userId);
            return false;
        }

        Users users = usersRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ResourceNotFound("User does not exist with id: " + userId));

        Optional<CartItem> cartItem = cartItemRepository.findByUsersAndProduct(users, product);
        if (cartItem.isPresent()) {
            CartItem existingItem = cartItem.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartRequest.getQuantity());
            existingItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingItem.getQuantity())));
            cartItemRepository.save(existingItem);

            logger.info("Updated existing cart item for userId={}, productId={}", userId, product.getId());
            return true;
        }

        CartItem newCartItem = new CartItem();
        newCartItem.setProduct(product);
        newCartItem.setUsers(users);
        newCartItem.setQuantity(cartRequest.getQuantity());
        newCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(cartRequest.getQuantity())));
        cartItemRepository.save(newCartItem);

        logger.info("Created new cart item for userId={}, productId={}", userId, product.getId());
        return true;
    }

    @Override
    public boolean removeItem(String userId, Long productId) {
        logger.info("Removing item from cart for userId={}, productId={}", userId, productId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFound("Product not found with id: " + productId));

        Users users = usersRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ResourceNotFound("User does not exist with id: " + userId));

        Optional<CartItem> cartItem = cartItemRepository.findByUsersAndProduct(users, product);
        if (cartItem.isPresent()) {
            cartItemRepository.delete(cartItem.get());
            logger.info("Removed cart item for userId={}, productId={}", userId, productId);
            return true;
        }

        logger.warn("No cart item found to remove for userId={}, productId={}", userId, productId);
        return false;
    }

    @Override
    public List<CartResponse> getCartItems(String userId) {
        logger.info("Fetching cart items for userId={}", userId);

        usersRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ResourceNotFound("User does not exist with id: " + userId));

        List<CartResponse> cartItems = cartItemRepository.findByUsersId(Long.valueOf(userId))
                .stream().map(cartMapper::toDto).toList();

        logger.info("Found {} cart items for userId={}", cartItems.size(), userId);
        return cartItems;
    }
}
