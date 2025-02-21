package com.ra.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserSignInRequestDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
