package com.ra.controller;

import com.ra.model.dto.request.UserSignInRequestDto;
import com.ra.model.dto.request.UserSignUpRequestDto;
import com.ra.model.dto.response.ApiResponse;
import com.ra.model.dto.response.UserResponseDto;
import com.ra.model.dto.response.UserSignInResponse;
import com.ra.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/sign-in")
    public ApiResponse<?> signIn(@RequestBody UserSignInRequestDto requestDto) {
        UserSignInResponse userLoginResponse = authService.login(requestDto);
        return new ApiResponse<>(200, "Dang nhap thanh cong", userLoginResponse);
    }

    @PostMapping("/sign-up")
    public ApiResponse<UserResponseDto> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        UserResponseDto userSignUpResponseDto = authService.register(requestDto);
        return new ApiResponse<>(200, "Dang ky thanh cong", userSignUpResponseDto);
    }

}
