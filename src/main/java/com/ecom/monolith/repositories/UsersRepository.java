package com.ecom.monolith.repositories;


import com.ecom.monolith.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Long> {
}
