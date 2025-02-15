package com.ra.model.dto;

import com.ra.model.entity.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserSignUpRequestDto {
    private String username;
    private String password;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private LocalDate createdAt;
}
