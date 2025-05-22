package com.ecom.monolith.service;

import com.ecom.monolith.exception.ResourceNotFound;
import com.ecom.monolith.model.Users;
import com.ecom.monolith.repositories.UsersRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UsersRepository usersRepository;

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public List<Users> getUsers() {
        return usersRepository.findAll();
    }

    @Override
    public List<Users> addUser(Users user) {
        usersRepository.save(user);
        return usersRepository.findAll();
    }

    @Override
    public Users findById(Long id) {
        return usersRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFound("User not found with id "+id)
        );
    }
}
