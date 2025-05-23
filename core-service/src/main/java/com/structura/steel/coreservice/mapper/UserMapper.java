package com.structura.steel.coreservice.mapper;

import com.structura.steel.coreservice.entity.User;
import com.structura.steel.commons.dto.core.request.CreateUserRequest;
import com.structura.steel.commons.dto.core.response.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    User toUser(CreateUserRequest request, String id);

    @Mapping(source = "user.id", target = "id")
    UserResponse toUserResponse(User user);
}
