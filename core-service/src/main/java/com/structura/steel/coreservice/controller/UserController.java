package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.response.RestResponse;
import com.structura.steel.commons.utils.SecurityUtils;
import com.structura.steel.dto.response.UserResponse;
import com.structura.steel.coreservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmailExist(@RequestParam String email) {
        return ResponseEntity.ok(userService.checkExistEmail(email));
    }

    @GetMapping("/my-profile")
    public ResponseEntity<RestResponse<UserResponse>> getUserProfile() {
        return ResponseEntity.ok(userService.getUser(SecurityUtils.getCurrentUserId()));
    }
}

