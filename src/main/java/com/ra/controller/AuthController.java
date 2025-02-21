package com.ra.controller;

import com.ra.model.dto.request.UserSignInRequestDto;
import com.ra.model.dto.response.UserSignInResponse;
import com.ra.model.dto.request.UserSignUpRequestDto;
import com.ra.model.dto.response.UserResponseDto;
import com.ra.service.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserSignInRequestDto requestDto) {
        UserSignInResponse userLoginResponse = authService.login(requestDto);
        return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
    }
    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDto> signUp(@Valid @RequestBody UserSignUpRequestDto requestDto) {
        UserResponseDto userSignUpResponseDto = authService.register(requestDto);
        return new ResponseEntity<>(userSignUpResponseDto, HttpStatus.OK);
    }

}
