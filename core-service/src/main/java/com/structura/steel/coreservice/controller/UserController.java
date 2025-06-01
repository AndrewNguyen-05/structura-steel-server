package com.structura.steel.coreservice.controller;

import com.structura.steel.commons.dto.core.request.CreateUserRequest;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.response.RestResponse;
import com.structura.steel.commons.utils.AppConstants;
import com.structura.steel.commons.utils.SecurityUtils;
import com.structura.steel.commons.dto.core.request.UpdateUserRequest;
import com.structura.steel.commons.dto.core.response.UserResponse;
import com.structura.steel.coreservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class    UserController {

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

    @GetMapping("/check-email")
    public ResponseEntity<String> checkEmailExist(@RequestParam String email) {
        return ResponseEntity.ok(userService.checkExistEmail(email));
    }

    @GetMapping("/my-profile")
    public ResponseEntity<RestResponse<UserResponse>> getUserProfile() {
        return ResponseEntity.ok(userService.getUser(SecurityUtils.getCurrentUserId()));
    }

    @GetMapping("/{userName}")
    public ResponseEntity<RestResponse<UserResponse>> getUserByUsername(@PathVariable String userName) {
        return ResponseEntity.ok(userService.getUserByUsername(userName));
    }

    @GetMapping
    public ResponseEntity<PagingResponse<UserResponse>> getAllUsers(
            @RequestParam(value = "pageNo", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER, required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = AppConstants.DEFAULT_PAGE_SIZE, required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstants.DEFAULT_SORT_BY, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstants.DEFAULT_SORT_DIRECTION, required = false) String sortDir
    ) {
        return ResponseEntity.ok(userService.getAllUsers(pageNo, pageSize, sortBy, sortDir));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<UserResponse>> updateUser(
            @PathVariable String id,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        RestResponse<UserResponse> updated = userService.updateUser(id, request);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}

