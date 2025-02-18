package com.ra.service.auth;

import com.ra.model.dto.*;

public interface AuthService {
    UserSignInResponse login(UserSignInRequestDto request);
    UserResponseDto register(UserSignUpRequestDto request);
    UserResponseDto updatePermission(UserPermissionDto request, Long userId) throws Exception;
    UserResponseDto removePermission(UserPermissionDto request, Long userId) throws Exception;
}
