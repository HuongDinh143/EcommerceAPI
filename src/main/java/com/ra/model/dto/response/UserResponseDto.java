package com.ra.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ra.model.entity.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private Boolean status;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Set<Role> roles;


}
