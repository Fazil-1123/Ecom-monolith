package com.ecom.monolith.service;

import com.ecom.monolith.model.Users;

import java.util.List;

public interface UserService {

    List<Users> getUsers();

    List<Users> addUser(Users user);

    Users findById(Long id);
}
