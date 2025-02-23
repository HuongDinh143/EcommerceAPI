package com.ra.service.user;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.ChangePasswordRequestDto;
import com.ra.model.dto.request.UserUpdateRequestDto;
import com.ra.model.dto.response.UserResponseDto;
import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserServiceImp implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
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

    @Override
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(()->new CustomException("User not found with id: "));
        return toDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new CustomException("User not found"));

        boolean isUpdated = false;

        if (requestDto.getUsername() != null && !requestDto.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(requestDto.getUsername())) {
                throw new CustomException("Username already exists");
            }
            user.setUsername(requestDto.getUsername());
            isUpdated = true;
        }

        if (requestDto.getEmail() != null ) {
            user.setEmail(requestDto.getEmail());
            isUpdated = true;
        }

        if (requestDto.getFullName() != null) {
            user.setFullName(requestDto.getFullName());
            isUpdated = true;
        }

        if (requestDto.getPhone() != null) {
            user.setPhone(requestDto.getPhone());
            isUpdated = true;
        }
        if (requestDto.getAddress() != null) {
            user.setAddress(requestDto.getAddress());
            isUpdated = true;
        }

        if (isUpdated) {
            user.setUpdatedAt(LocalDate.now());
            userRepository.save(user);
            return toDto(user);
        }
        return null;

    }

    @Transactional
    public void changePassword(Long userId, ChangePasswordRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        if (!passwordEncoder.matches(requestDto.getOldPassword(), user.getPassword())) {
            throw new CustomException("Old password is incorrect");
        }

        if (!requestDto.getNewPassword().equals(requestDto.getConfirmPassword())) {
            throw new CustomException("New password and confirmation do not match");
        }

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);
    }


    private UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .address(user.getAddress())
                .roles(user.getRoles())
                .build();
    }
}
