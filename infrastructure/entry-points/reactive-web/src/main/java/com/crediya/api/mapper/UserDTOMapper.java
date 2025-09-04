package com.crediya.api.mapper;

import com.crediya.api.dto.CreateUserDTO;
import com.crediya.api.dto.UserDTO;
import com.crediya.model.user.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDTOMapper {
    UserDTO toResponse(User user);
    List<UserDTO> toResponseList(List<User> users);
    User toModel(CreateUserDTO createUserDTO);
}