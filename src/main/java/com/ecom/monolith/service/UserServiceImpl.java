package com.ecom.monolith.service;

import com.ecom.monolith.Dto.UsersDto;
import com.ecom.monolith.Mapper.UserMapper;
import com.ecom.monolith.exception.ResourceNotFound;
import com.ecom.monolith.model.Users;
import com.ecom.monolith.repositories.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UsersRepository usersRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UsersRepository usersRepository, UserMapper userMapper) {
        this.usersRepository = usersRepository;
        this.userMapper = userMapper;
    }

    @Override
    public List<UsersDto> getUsers() {
        logger.info("Fetching all users");
        List<UsersDto> usersDtos = usersRepository.findAll()
                .stream().map(userMapper::toDto).toList();
        logger.info("Fetched {} users", usersDtos.size());
        return usersDtos;
    }

    @Override
    public UsersDto addUser(UsersDto user) {
        logger.info("Adding new user with email={}", user.getEmail());
        Users userToSave = userMapper.toEntity(user);
        Users savedUser = usersRepository.save(userToSave);
        logger.info("User added with ID={}", savedUser.getId());
        return userMapper.toDto(savedUser);
    }

    @Override
    public UsersDto findById(Long id) {
        logger.info("Fetching user by ID={}", id);
        return usersRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> {
                    logger.warn("User not found with ID={}", id);
                    return new ResourceNotFound("User not found with id " + id);
                });
    }

    @Override
    public UsersDto updateUser(Long id, UsersDto users) {
        logger.info("Updating user ID={}", id);
        Users userUpdate = userMapper.toEntity(users);

        return usersRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setFirstName(userUpdate.getFirstName());
                    existingUser.setLastName(userUpdate.getLastName());
                    Users savedUser = usersRepository.save(existingUser);
                    logger.info("User updated successfully: ID={}", savedUser.getId());
                    return userMapper.toDto(savedUser);
                }).orElseThrow(() -> {
                    logger.warn("User not found for update: ID={}", id);
                    return new ResourceNotFound("User not found with id " + id);
                });
    }
}
