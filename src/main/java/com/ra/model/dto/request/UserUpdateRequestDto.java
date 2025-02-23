package com.ra.model.dto.request;

import com.ra.validate.UserUnique;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserUpdateRequestDto {
    @UserUnique
    @Size(min = 6, max = 100,message = "tên tài khoản có ít nhất 6 ký tự")
    private String username;
    @Email
    private String email;
    private String fullName;
    @UserUnique()
    @Pattern(regexp = "^(?:\\+84|0)(?:3[2-9]|5[2689]|7[06-9]|8[1-9]|9[0-9])[0-9]{7}$",message = "Sai định dạng số điện thoại")
    private String phone;
    private String address;
    private LocalDate updatedAt;
}
