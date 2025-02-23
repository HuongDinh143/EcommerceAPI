package com.ra.service.user;

import com.ra.model.dto.request.ChangePasswordRequestDto;
import com.ra.model.dto.request.UserUpdateRequestDto;
import com.ra.model.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponseDto> pagination(Pageable pageable);
    void updateStatusUser(Long userId) throws Exception;
    UserResponseDto findByName(String username);
    UserResponseDto findById(Long id);
    UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto);
    void changePassword(Long userId, ChangePasswordRequestDto requestDto);
}
