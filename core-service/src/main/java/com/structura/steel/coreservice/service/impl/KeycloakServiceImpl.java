package com.structura.steel.coreservice.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.structura.steel.commons.exception.DuplicateKeyException;
import com.structura.steel.coreservice.config.property.KeycloakProperty;
import com.structura.steel.dto.request.CreateUserRequest;
import com.structura.steel.coreservice.service.KeycloakService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

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

        try (Response response = realmResource.users().create(userRepresentation)) {
            String responseBody = response.readEntity(String.class);
            if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
                String parsedError = parseErrorMessage(responseBody);
                throw new DuplicateKeyException("Failed to create user: " + parsedError);
            }
            log.info("Create user response from Keycloak: status {}, body {}", response.getStatus(), response.readEntity(String.class));

            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
            credentialRepresentation.setValue(request.password());
            credentialRepresentation.setTemporary(false);

            String id = CreatedResponseUtil.getCreatedId(response);
            UserResource userResource = realmResource.users().get(id);
            userResource.resetPassword(credentialRepresentation);

            return id;
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

}
