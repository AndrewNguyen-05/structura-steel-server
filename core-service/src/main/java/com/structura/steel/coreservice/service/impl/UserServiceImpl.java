package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.RestResponse;
import com.structura.steel.dto.request.CreateUserRequest;
import com.structura.steel.dto.response.UserResponse;
import com.structura.steel.coreservice.entity.User;
import com.structura.steel.coreservice.mapper.UserMapper;
import com.structura.steel.coreservice.repository.UserRepository;
import com.structura.steel.coreservice.service.KeycloakService;
import com.structura.steel.coreservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @Override
    @Transactional
    public RestResponse<UserResponse> createUser(CreateUserRequest request) {
        String id = keycloakService.createUser(request);
        try {
            User user = userRepository.saveAndFlush(userMapper.toUser(request, id));

            return RestResponse.created(userMapper.toUserResponse(user));
        } catch (Exception e) {
            keycloakService.deleteUser(id);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RestResponse<UserResponse> getUser(String id) {

        return userRepository.findById(id)
                .map(userMapper::toUserResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public void isEmailExist(String email) {
        if (keycloakService.isEmailExist(email)) {
            throw new RuntimeException("Email already exists");
        }
    }
}

