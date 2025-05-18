package com.ecom.monolith.controller;

import com.ecom.monolith.model.Users;
import com.ecom.monolith.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/api/users")
    public List<Users> getUsers(){
        return userService.getUsers();
    }

    @PostMapping("api/users")
    public List<Users> addUser(@RequestBody Users user){
        return userService.addUser(user);
    }

    @GetMapping("/api/users/{id}")
    public Users findById(@PathVariable("id") Long id){
        return userService.findById(id);
    }
}
