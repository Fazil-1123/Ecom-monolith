package com.ecom.monolith.repositories;


import com.ecom.monolith.model.Users;
import org.h2.engine.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users,Long> {
}
