package com.ecom.monolith.controller;

import com.ecom.monolith.Dto.UsersDto;
import com.ecom.monolith.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<UsersDto> getUsers() {
        logger.info("GET /api/users - Fetching all users");
        return userService.getUsers();
    }

    @PostMapping()
    public UsersDto addUser(@Valid @RequestBody UsersDto user) {
        logger.info("POST /api/users - Adding new user with email={}", user.getEmail());
        return userService.addUser(user);
    }

    @GetMapping("{id}")
    public UsersDto findById(@PathVariable("id") Long id) {
        logger.info("GET /api/users/{} - Fetching user by ID", id);
        return userService.findById(id);
    }

    @PutMapping("{id}")
    public UsersDto updateUser(@PathVariable("id") Long id, @Valid @RequestBody UsersDto users) {
        logger.info("PUT /api/users/{} - Updating user", id);
        return userService.updateUser(id, users);
    }
}
