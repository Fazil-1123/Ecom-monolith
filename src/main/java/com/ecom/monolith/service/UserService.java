package com.ecom.monolith.service;

import com.ecom.monolith.Dto.UsersDto;
import com.ecom.monolith.model.Users;

import java.util.List;

public interface UserService {

    List<UsersDto> getUsers();

    UsersDto addUser(UsersDto user);

    UsersDto findById(Long id);

    UsersDto updateUser(Long id, UsersDto users);
}
