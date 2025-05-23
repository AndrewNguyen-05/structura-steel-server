package com.structura.steel.coreservice.service.impl;

import com.structura.steel.commons.exception.DuplicateKeyException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.response.RestResponse;
import com.structura.steel.commons.dto.core.request.CreateUserRequest;
import com.structura.steel.commons.dto.core.request.UpdateUserRequest;
import com.structura.steel.commons.dto.core.response.UserResponse;
import com.structura.steel.coreservice.entity.User;
import com.structura.steel.coreservice.mapper.UserMapper;
import com.structura.steel.coreservice.repository.UserRepository;
import com.structura.steel.coreservice.service.KeycloakService;
import com.structura.steel.coreservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public RestResponse<UserResponse> getUserByUsername(String userName) {

        return userRepository.findByUsername(userName)
                .map(userMapper::toUserResponse)
                .map(RestResponse::ok)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", userName));
    }


    @Override
    public String checkExistEmail(String email) {
        if (keycloakService.isEmailExist(email)) {
            throw new DuplicateKeyException("User", "email", email);
        }
        return "This email is available";
    }

    @Override
    public PagingResponse<UserResponse> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir) {
        List<UserRepresentation> kcUsers = keycloakService.getListUsers();

        for (UserRepresentation kcUser : kcUsers) {
            if (!userRepository.existsById(kcUser.getId())) {
                User newUser = new User();
                newUser.setId(kcUser.getId());
                newUser.setUsername(kcUser.getUsername());
                newUser.setEmail(kcUser.getEmail());
                newUser.setFirstName(kcUser.getFirstName());
                newUser.setLastName(kcUser.getLastName());
                newUser.setRealmRole(kcUser.getRealmRoles().get(0));
                userRepository.save(newUser);
            }
        }

        // Tao sort
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // Tao 1 pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        // Tao 1 mang cac trang product su dung find all voi tham so la pageable
        Page<User> pages = userRepository.findAll(pageable);

        // Lay ra gia tri (content) cua page
        List<User> products = pages.getContent();


        // Ep kieu sang dto
        List<UserResponse> content = products.stream().map(userMapper::toUserResponse).collect(Collectors.toList());

        // Gan gia tri (content) cua page vao ProductResponse de tra ve
        PagingResponse<UserResponse> response = new PagingResponse<>();
        response.setContent(content);
        response.setTotalElements(pages.getTotalElements());
        response.setPageNo(pages.getNumber());
        response.setPageSize(pages.getSize());
        response.setTotalPages(pages.getTotalPages());
        response.setLast(pages.isLast());

        return response;
    }

    @Override
    @Transactional
    public RestResponse<UserResponse> updateUser(String id, UpdateUserRequest request) {
        // 1) Update user trong Keycloak
        keycloakService.updateUser(id, request);

        // 2) Update user trong DB
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setRealmRole(request.realmRole());

        userRepository.saveAndFlush(user);

        return RestResponse.ok(userMapper.toUserResponse(user));
    }

    @Override
    @Transactional
    public void deleteUser(String id) {
        keycloakService.deleteUser(id);

        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void syncAllUsersFromKeycloak() {
        // Lưu lại Authentication hiện tại (nếu có)
        var originalAuth = SecurityContextHolder.getContext().getAuthentication();
        // Thiết lập một Authentication tạm thời với tên là "System"
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("System", null, List.of())
        );
        try {
            long count = userRepository.count();
            if (count == 0) {
                // DB trống => quét Keycloak và thêm vào DB
                List<UserRepresentation> kcUsers = keycloakService.getListUsers();
                for (UserRepresentation kcUser : kcUsers) {
                    if (kcUser.getUsername().equalsIgnoreCase("service-account-admin-cli")) {
                        continue;
                    }

                    //get roles
                    List<String> names = keycloakService
                            .getListRealmRolesByUserId(kcUser.getId())
                            .stream()
                            .map(RoleRepresentation::getName)
                            .filter(name -> name.startsWith("ROLE_"))
                            .toList();

                    User user = new User();
                    user.setId(kcUser.getId());
                    user.setUsername(kcUser.getUsername());
                    user.setFirstName(kcUser.getFirstName());
                    user.setLastName(kcUser.getLastName());
                    user.setEmail(kcUser.getEmail());
                    user.setRealmRole(!names.isEmpty() ? names.get(0) : null);
                    userRepository.saveAndFlush(user);
                }
            }
        } finally {
            // Khôi phục lại Authentication ban đầu sau khi đồng bộ
            SecurityContextHolder.getContext().setAuthentication(originalAuth);
        }
    }
}

