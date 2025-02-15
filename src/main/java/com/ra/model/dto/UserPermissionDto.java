package com.ra.model.dto;

import com.ra.model.entity.Role;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserPermissionDto {
    private Set<String> roleName;
}
