package com.ecom.monolith.Mapper;

import com.ecom.monolith.Dto.UsersDto;
import com.ecom.monolith.model.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    Users toEntity(UsersDto dto);

    UsersDto toDto(Users user);
}
