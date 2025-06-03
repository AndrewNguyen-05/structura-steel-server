package com.structura.steel.coreservice.service;

import com.structura.steel.commons.dto.core.request.authentication.CreateUserRequest;
import com.structura.steel.commons.dto.core.request.authentication.UpdateUserRequest;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public interface KeycloakService {

    String createUser(CreateUserRequest request);

    List<UserRepresentation> getListUsers();

    UserRepresentation getUserById(String id);

    List<RoleRepresentation> getListRealmRoles();

    List<RoleRepresentation> getListRealmRolesByUserId(String id);

    List<RoleRepresentation> getListClientRolesByUserId(String id);

    void deleteUser(String id);

    boolean isEmailExist(String email);

    void updateUser(String id, UpdateUserRequest request);

    void resetPassword(String email, String newPassword);

    void firstTimePasswordChange(String email, String temporaryPassword, String newPassword);
}
