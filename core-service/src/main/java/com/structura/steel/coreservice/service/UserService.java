package com.structura.steel.coreservice.service;

import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.response.RestResponse;
import com.structura.steel.dto.request.CreateUserRequest;
import com.structura.steel.dto.request.UpdateUserRequest;
import com.structura.steel.dto.response.UserResponse;

import java.util.List;

public interface UserService {
    RestResponse<UserResponse> createUser(CreateUserRequest request);
    RestResponse<UserResponse> getUser(String id);
    RestResponse<UserResponse> getUserByUsername(String userName);
    String checkExistEmail(String email);
    PagingResponse<UserResponse> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);
    RestResponse<UserResponse> updateUser(String id, UpdateUserRequest request);
    void deleteUser(String id);
    void syncAllUsersFromKeycloakIfDbEmpty();
}
