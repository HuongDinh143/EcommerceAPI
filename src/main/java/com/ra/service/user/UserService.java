package com.ra.service.user;

import com.ra.model.dto.response.UserResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponseDto> pagination(Pageable pageable);
    void updateStatusUser(Long userId) throws Exception;
    UserResponseDto findByName(String username);
}
