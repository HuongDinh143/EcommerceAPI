package com.ra.model.dto;

import com.ra.model.entity.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class UserResponseDto {
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private LocalDate createdAt;
    private Set<Role> roles;


}
