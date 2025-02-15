package com.ra.model.dto;

import com.ra.model.entity.Role;
import lombok.*;

import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginResponse {
    private String username;
    private String typeToken;
    private String accessToken;
    private Set<Role> roles;

}
