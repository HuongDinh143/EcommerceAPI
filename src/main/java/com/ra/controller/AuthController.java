package com.ra.controller;

import com.ra.model.dto.UserLoginRequestDto;
import com.ra.model.dto.UserLoginResponse;
import com.ra.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto requestDto) {
        UserLoginResponse userLoginResponse = authService.login(requestDto);
        return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
    }
}
