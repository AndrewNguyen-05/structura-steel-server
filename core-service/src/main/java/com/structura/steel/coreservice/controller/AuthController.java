package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.response.RestResponse;
import com.structura.steel.commons.dto.core.request.authentication.CreateUserRequest;
import com.structura.steel.commons.dto.core.response.UserResponse;
import com.structura.steel.coreservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping(
            value = "/sign-up",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RestResponse<UserResponse>> createUser(@RequestBody @Valid CreateUserRequest request) {
        RestResponse<UserResponse> response = userService.createUser(request);
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{id}")
                        .buildAndExpand(response.data().id())
                        .toUri())
                .body(response);
    }
}

