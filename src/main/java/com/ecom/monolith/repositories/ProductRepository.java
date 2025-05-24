package com.ecom.monolith.repositories;

import com.ecom.monolith.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String keyword);

    List<Product> findByActiveTrue();
}
