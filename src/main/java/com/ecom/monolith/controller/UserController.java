package com.ecom.monolith.controller;

import com.ecom.monolith.Dto.UsersDto;
import com.ecom.monolith.model.Users;
import com.ecom.monolith.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping()
    public List<UsersDto> getUsers(){
        return userService.getUsers();
    }

    @PostMapping()
    public UsersDto addUser(@Valid @RequestBody UsersDto user){
        return userService.addUser(user);
    }

    @GetMapping("{id}")
    public UsersDto findById(@PathVariable("id") Long id){
        return userService.findById(id);
    }

    @PutMapping("{id}")
    public UsersDto updateUser(@Valid @PathVariable("id") Long id, @RequestBody UsersDto users){
        return userService.updateUser(id, users);
    }
}
