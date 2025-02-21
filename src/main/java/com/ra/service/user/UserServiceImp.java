package com.ra.service.user;

import com.ra.model.dto.response.UserResponseDto;
import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public Page<UserResponseDto> pagination(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        List<UserResponseDto> userResponseDtos = users.getContent()
                .stream().map(this::toDto).toList();
        return new PageImpl<>(userResponseDtos, pageable, users.getTotalElements());
    }

    @Override
    public void updateStatusUser(Long userId) throws Exception {
        User user = userRepository.findById(userId).orElseThrow(()->new Exception("User not found"));
        user.setStatus(!user.getStatus());
        userRepository.save(user);
    }

    @Override
    public UserResponseDto findByName(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        if (user == null) {
            return null;
        }
        return toDto(user);

    }

    private UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .roles(user.getRoles())
                .build();
    }
}
