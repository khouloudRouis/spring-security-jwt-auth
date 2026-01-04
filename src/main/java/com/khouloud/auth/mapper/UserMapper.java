package com.khouloud.auth.mapper;

import org.mapstruct.Mapper;

import com.khouloud.auth.dto.UserDto;
import com.khouloud.auth.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User entity);
 
}
