package com.ra.model.dto;


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
