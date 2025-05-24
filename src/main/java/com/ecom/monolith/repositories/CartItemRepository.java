package com.ecom.monolith.repositories;

import com.ecom.monolith.model.CartItem;
import com.ecom.monolith.model.Product;
import com.ecom.monolith.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    Optional<CartItem> findByUsersAndProduct(Users users, Product product);

    List<CartItem> findByUsersId(Long userId);
}
