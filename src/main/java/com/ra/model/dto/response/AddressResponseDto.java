package com.ra.model.dto.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddressResponseDto {
    private Long addressId;
    private String fullAddress;
    private String phone;
    private String receiveName;

}
