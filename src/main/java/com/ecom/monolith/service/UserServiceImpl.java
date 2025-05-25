package com.ecom.monolith.service;

import com.ecom.monolith.Dto.UsersDto;
import com.ecom.monolith.Mapper.UserMapper;
import com.ecom.monolith.exception.ResourceNotFound;
import com.ecom.monolith.model.Users;
import com.ecom.monolith.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UsersRepository usersRepository, UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UsersDto> getUsers() {
        List<UsersDto> usersDtos= usersRepository.findAll()
                .stream().map(userMapper::toDto).toList();
        return usersDtos;
    }

    @Override
    public UsersDto addUser(UsersDto user) {
        Users userToSave = userMapper.toEntity(user);
        Users savedUser = usersRepository.save(userToSave);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UsersDto findById(Long id) {
        return usersRepository.findById(id)
                .map(users -> userMapper.toDto(users)).orElseThrow(
                () -> new ResourceNotFound("User not found with id " + id)
        );
    }

    @Override
    public UsersDto updateUser(Long id, UsersDto users) {
        Users userUpdate = userMapper.toEntity(users);
        return usersRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(userUpdate.getFirstName());
                    existingUser.setLastName(userUpdate.getLastName());
                    Users savedUser = usersRepository.save(existingUser);
                    return userMapper.toDto(savedUser);
                }).orElseThrow(()-> new ResourceNotFound("User not found with id " + id));

    }
}
