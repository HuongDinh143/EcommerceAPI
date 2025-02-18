package com.ra.service.auth;

import com.ra.model.dto.*;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
import com.ra.repository.RoleRepository;
import com.ra.repository.UserRepository;
import com.ra.security.UserPrinciple;
import com.ra.security.jwt.JwtProvider;
import com.ra.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImp implements AuthService {
    @Autowired
    private AuthenticationProvider authenticationProvider;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UploadService uploadService;
    @Override
    public UserSignInResponse login(UserSignInRequestDto request) {
        Authentication authentication;
        authentication = authenticationProvider.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        return UserSignInResponse.builder()
                .username(userPrinciple.getUsername())
                .typeToken("Bearer Token")
                .accessToken(jwtProvider.generateToken(userPrinciple))
                .roles(userPrinciple.getUser().getRoles())
                .build();
    }

    @Override
    public UserResponseDto register(UserSignUpRequestDto request) {
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByRoleName(Role.RoleName.valueOf("USER"));
        roles.add(role);

        User user = User.builder()
                .username(request.getUsername())
                .password(new BCryptPasswordEncoder().encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .address(request.getAddress())
                .createdAt(LocalDate.now())
                .status(true)
                .isDeleted(false)
                .roles(roles)
                .build();
        userRepository.save(user);
        return toDto(user);
    }

    @Override
    public UserResponseDto updatePermission(UserPermissionDto request, Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        Set<Role> currentRoles = user.getRoles();

        for (String roleName : request.getRoleName()) {
            Role role = roleRepository.findByRoleName(Role.RoleName.valueOf(roleName));
            currentRoles.add(role);
        }

        user.setRoles(currentRoles);
        User updatedUser = userRepository.save(user);

        return toDto(updatedUser);
    }

    @Override
    public UserResponseDto removePermission(UserPermissionDto request, Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));

        Set<Role> currentRoles = user.getRoles();

        for (String roleName : request.getRoleName()) {
            Role role = roleRepository.findByRoleName(Role.RoleName.valueOf(roleName));
            currentRoles.remove(role);
        }

        user.setRoles(currentRoles);
        User updatedUser = userRepository.save(user);

        return toDto(updatedUser);
    }


    private UserResponseDto toDto(User user) {
        return UserResponseDto.builder()
                .username(user.getUsername())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .roles(user.getRoles())
                .build();

    }
}
