package com.ra.model.dto.request;

import com.ra.validate.UserUnique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserSignUpRequestDto {
    @NotBlank
    @Size(min = 6, max = 100,message = "Tối thiểu 6 ký tự, tối đa 100 kí tự")
    @UserUnique()
    private String username;
    private String password;
    @Email
    private String email;
    @NotBlank
    private String fullName;
    @UserUnique()
    @Pattern(regexp = "^(?:\\+84|0)(?:3[2-9]|5[2689]|7[06-9]|8[1-9]|9[0-9])[0-9]{7}$",message = "Sai định dạng số điện thoại")
    private String phone;
    @NotBlank
    private String address;
    private LocalDate createdAt;
}
