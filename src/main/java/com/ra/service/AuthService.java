package com.ra.service;

import com.ra.model.dto.*;

public interface AuthService {
    UserSignInResponse login(UserSignInRequestDto request);
    UserSignUpResponseDto register(UserSignUpRequestDto request);
    UserSignUpResponseDto updatePermission(UserPermissionDto request,Long userId) throws Exception;
}
