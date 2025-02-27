package com.ra.service.auth;

import com.ra.model.dto.*;
import com.ra.model.dto.request.UserSignInRequestDto;
import com.ra.model.dto.request.UserSignUpRequestDto;
import com.ra.model.dto.response.RevenueByCategoryDto;
import com.ra.model.dto.response.UserResponseDto;
import com.ra.model.dto.response.UserSignInResponse;

import java.time.LocalDate;
import java.util.List;

public interface AuthService {
    UserSignInResponse login(UserSignInRequestDto request);
    UserResponseDto register(UserSignUpRequestDto request);
    UserResponseDto updatePermission(UserPermissionDto request, Long userId) ;
    UserResponseDto removePermission(UserPermissionDto request, Long userId) ;

}
