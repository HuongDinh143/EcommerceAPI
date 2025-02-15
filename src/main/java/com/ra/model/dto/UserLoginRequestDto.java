package com.ra.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserLoginRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
