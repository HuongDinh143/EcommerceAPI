package com.ra.service;

import com.ra.model.dto.UserLoginRequestDto;
import com.ra.model.dto.UserLoginResponse;
import com.ra.security.UserPrinciple;
import com.ra.security.jwt.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImp implements AuthService {
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtProvider jwtProvider;
    @Override
    public UserLoginResponse login(UserLoginRequestDto request) {
        Authentication authentication;
        authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return UserLoginResponse.builder()
                .username(userPrinciple.getUsername())
                .typeToken("Bearer Token")
                .accessToken(jwtProvider.generateToken(userPrinciple))
                .build();
    }
}
