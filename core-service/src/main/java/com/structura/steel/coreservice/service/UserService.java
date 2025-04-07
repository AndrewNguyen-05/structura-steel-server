package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.RestResponse;
import com.structura.steel.dto.request.CreateUserRequest;
import com.structura.steel.dto.response.UserResponse;

public interface UserService {
    RestResponse<UserResponse> createUser(CreateUserRequest request);
    RestResponse<UserResponse> getUser(String id);
    String checkExistEmail(String email);
}
