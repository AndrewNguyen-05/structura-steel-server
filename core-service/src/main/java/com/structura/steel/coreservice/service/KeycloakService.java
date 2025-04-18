package com.structura.steel.coreservice.service;

import com.structura.steel.dto.request.CreateUserRequest;
import com.structura.steel.dto.request.UpdateUserRequest;
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
}
