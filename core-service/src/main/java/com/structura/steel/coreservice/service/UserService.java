package com.structura.steel.coreservice.service;

import com.structura.steel.commons.dto.core.request.authentication.FirstTimePasswordChangeRequest;
import com.structura.steel.commons.dto.core.request.authentication.ForgotPasswordRequest;
import com.structura.steel.commons.dto.core.request.authentication.ResetPasswordRequest;
import com.structura.steel.commons.dto.core.request.authentication.VerifyOtpRequest;
import com.structura.steel.commons.response.PagingResponse;
import com.structura.steel.commons.response.RestResponse;
import com.structura.steel.commons.dto.core.request.authentication.CreateUserRequest;
import com.structura.steel.commons.dto.core.request.authentication.UpdateUserRequest;
import com.structura.steel.commons.dto.core.response.UserResponse;

public interface UserService {

    RestResponse<UserResponse> createUser(CreateUserRequest request);

    RestResponse<UserResponse> getUser(String id);

    RestResponse<UserResponse> getUserByUsername(String userName);

    String checkExistEmail(String email);

    PagingResponse<UserResponse> getAllUsers(int pageNo, int pageSize, String sortBy, String sortDir);

    RestResponse<UserResponse> updateUser(String id, UpdateUserRequest request);

    void deleteUser(String id);

    void syncAllUsersFromKeycloak();

    RestResponse<String> firstTimePasswordChange(FirstTimePasswordChangeRequest request);

    RestResponse<String> initiateForgotPassword(ForgotPasswordRequest request);

    RestResponse<String> verifyOtp(VerifyOtpRequest request);

    RestResponse<String> resetPassword(ResetPasswordRequest request);
}
