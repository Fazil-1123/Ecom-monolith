package com.ecom.monolith.repositories;

import com.ecom.monolith.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
