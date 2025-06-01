package com.structura.steel.coreservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.structura.steel.commons.exception.DuplicateKeyException;
import com.structura.steel.commons.exception.ResourceNotFoundException;
import com.structura.steel.coreservice.config.property.KeycloakProperty;
import com.structura.steel.commons.dto.core.request.CreateUserRequest;
import com.structura.steel.coreservice.service.KeycloakService;
import com.structura.steel.commons.dto.core.request.UpdateUserRequest;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    private final KeycloakProperty keycloak;
    private final RealmResource realmResource;

    @Override
    public String createUser(CreateUserRequest request) {
        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(request.username());
        userRepresentation.setFirstName(request.firstName());
        userRepresentation.setLastName(request.lastName());
        userRepresentation.setEmail(request.email());
        userRepresentation.setEmailVerified(true);
        userRepresentation.setEnabled(true);

        String userId = null;
        try (Response response = realmResource.users().create(userRepresentation)) {
            String responseBody = response.readEntity(String.class);
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                String parsedError = parseErrorMessage(responseBody);
                log.error("Failed to create user in Keycloak: status {}, body {}", response.getStatus(), responseBody);
                throw new DuplicateKeyException("Failed to create user: " + parsedError);
            }
            log.info("Create user response from Keycloak: status {}, body {}", response.getStatus(), responseBody);

            userId = CreatedResponseUtil.getCreatedId(response);
            UserResource userResource = realmResource.users().get(userId);

            // Đặt mật khẩu
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(request.password());
            credentialRepresentation.setTemporary(false);
            userResource.resetPassword(credentialRepresentation);

            // Gán Realm Roles
            assignRealmRoleToUser(userResource, request.realmRole());

            return userId;
        } catch (Exception e) {
            // Nếu có lỗi xảy ra (bao gồm cả lỗi gán role), xóa user vừa tạo trên Keycloak nếu đã tạo thành công
            if (userId != null) {
                log.warn("Error occurred during user creation process (possibly role assignment). Rolling back Keycloak user creation for id: {}", userId);
                try {
                    realmResource.users().get(userId).remove();
                    log.info("Successfully rolled back Keycloak user creation for id: {}", userId);
                } catch (Exception deleteEx) {
                    log.error("Failed to rollback Keycloak user creation for id: {}. Error: {}", userId, deleteEx.getMessage(), deleteEx);
                }
            }
            log.error("Error during user creation or role assignment: {}", e.getMessage(), e);

            throw new RuntimeException("Error during user creation or role assignment: " + e.getMessage(), e);
        }
    }

    @Override
    public List<UserRepresentation> getListUsers() {
        try {
            return realmResource.users().list();
        } catch (Exception e) {
            log.error("Error when getting users from keycloak: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserRepresentation getUserById(String id) {
        try {
            return realmResource.users().get(id).toRepresentation();
        } catch (Exception e) {
            log.error("Error when getting user by id from keycloak: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RoleRepresentation> getListRealmRoles() {
        try {
            return realmResource.roles().list();
        } catch (Exception e) {
            log.error("Error when getting realm roles from keycloak: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RoleRepresentation> getListRealmRolesByUserId(String id) {
        try {
            return realmResource.users().get(id).roles().realmLevel().listEffective();
        } catch (Exception e) {
            log.error("Error when getting user's realm roles from keycloak: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RoleRepresentation> getListClientRolesByUserId(String id) {
        try {
            return realmResource.users().get(id).roles().clientLevel(realmResource.clients()
                    .findByClientId(keycloak.client().structura()).get(0).getId()).listEffective();
        } catch (Exception e) {
            log.error("Error when getting user's client roles from keycloak: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(String id) {
        try {
            realmResource.users().delete(id).close();
        } catch (Exception e) {
            log.error("Error when delete user by id: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isEmailExist(String email) {
        try {
            return !realmResource.users().searchByEmail(email, true).isEmpty();
        } catch (Exception e) {
            log.error("Error when delete user by id: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(String id, UpdateUserRequest request) {
        UserResource userResource = realmResource.users().get(id);
        UserRepresentation userRepresentation = userResource.toRepresentation();

        if(!request.email().equals(userRepresentation.getEmail()) &&
                isEmailExist(request.email())) {
            throw new DuplicateKeyException("Email already exists");
        }

        userRepresentation.setFirstName(request.firstName());
        userRepresentation.setLastName(request.lastName());
        userRepresentation.setEmail(request.email());

        try {
            userResource.update(userRepresentation);
        } catch (Exception e) {
            throw new RuntimeException("Error when update user by id: " + id);
        }

        if (request.password() != null && !request.password().isBlank()) {
            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(request.password());
            credential.setTemporary(false);
            userResource.resetPassword(credential);
        }

        // Cập nhật Realm Roles
        updateUserRealmRole(userResource, request.realmRole());
    }

    private String parseErrorMessage(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(json);
            if (root.has("errorMessage")) {
                return root.get("errorMessage").asText();
            }
            return json;
        } catch (Exception e) {
            // Nếu parse thất bại, trả về nguyên chuỗi gốc
            return json;
        }
    }

    private RoleRepresentation validateAndGetRealmRole(String roleName) {
        if (!StringUtils.hasText(roleName)) {
            return null;
        }

        RolesResource rolesResource = realmResource.roles();
        try {
            // Lấy role representation từ tên
            return rolesResource.get(roleName).toRepresentation();
        } catch (NotFoundException e) {
            log.error("Realm role '{}' not found.", roleName);
            throw new ResourceNotFoundException("Realm Role", "name", roleName);
        } catch (Exception e) {
            log.error("Error fetching realm role '{}': {}", roleName, e.getMessage(), e);
            throw new RuntimeException("Error fetching realm role: " + roleName, e);
        }
    }

    private void assignRealmRoleToUser(UserResource userResource, String roleName) {
        RoleRepresentation roleToAssign = validateAndGetRealmRole(roleName);
        if (roleToAssign != null) {
            // Gán role bằng cách thêm vào danh sách (chứa 1 phần tử)
            userResource.roles().realmLevel().add(Collections.singletonList(roleToAssign));
            log.info("Assigned realm role '{}' to user id: {}", roleName, userResource.toRepresentation().getId());
        }
    }

    private void updateUserRealmRole(UserResource userResource, String updatedRoleName) {
        // 1. Lấy tất cả các realm roles hiện tại của user
        List<RoleRepresentation> currentRoles = userResource.roles().realmLevel().listEffective();

        // 2. Xác thực role
        RoleRepresentation desiredRole = validateAndGetRealmRole(updatedRoleName);

        boolean needsUpdate = false;
        List<RoleRepresentation> rolesToAdd = new ArrayList<>();
        List<RoleRepresentation> rolesToRemove = new ArrayList<>(currentRoles); // Mặc định xóa hết role cũ

        if (desiredRole != null) {
            final String finalDesiredRoleName = desiredRole.getName(); // Để sử dụng trong lambda
            boolean alreadyHasRole = currentRoles.stream().anyMatch(r -> r.getName().equals(finalDesiredRoleName));

            if (currentRoles.size() != 1 || !alreadyHasRole) {
                // Cần cập nhật nếu:
                // - User đang có nhiều hơn 1 role (cần dọn dẹp)
                // - User đang có 0 role
                // - User đang có 1 role nhưng khác role mong muốn
                needsUpdate = true;
                rolesToAdd.add(desiredRole);
            }
            // Nếu user đã có đúng 1 role và đó chính là role mong muốn -> không cần làm gì
        }

        if (needsUpdate) {
            log.info("Updating realm roles for user id: {}", userResource.toRepresentation().getId());
            // 3. Xóa các role cũ (luôn xóa hết để đảm bảo sạch sẽ)
            if (!rolesToRemove.isEmpty()) {
                userResource.roles().realmLevel().remove(rolesToRemove);
                log.info("Removed existing realm roles: {} for user id: {}",
                        rolesToRemove.stream().map(RoleRepresentation::getName).collect(Collectors.toList()),
                        userResource.toRepresentation().getId());
            }

            // 4. Thêm role mới (nếu có)
            if (!rolesToAdd.isEmpty()) {
                userResource.roles().realmLevel().add(rolesToAdd); // rolesToAdd chỉ chứa tối đa 1 role
                log.info("Added realm role: '{}' for user id: {}",
                        rolesToAdd.get(0).getName(), userResource.toRepresentation().getId());
            }
        } else {
            log.info("Realm role for user id {} is already up-to-date.", userResource.toRepresentation().getId());
        }
    }

}
