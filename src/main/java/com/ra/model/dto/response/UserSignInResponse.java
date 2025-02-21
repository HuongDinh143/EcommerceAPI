package com.ra.model.dto.response;

import com.ra.model.entity.Role;
import lombok.*;

import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserSignInResponse {
    private String username;
    private String typeToken;
    private String accessToken;
    private Set<Role> roles;

}
