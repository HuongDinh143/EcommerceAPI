package com.ra.service;

import com.ra.model.dto.UserLoginRequestDto;
import com.ra.model.dto.UserLoginResponse;

public interface AuthService {
    UserLoginResponse login(UserLoginRequestDto request);
}
