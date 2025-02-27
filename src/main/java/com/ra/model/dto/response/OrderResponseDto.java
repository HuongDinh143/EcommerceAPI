package com.ra.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponseDto {
    private Long id;
    private String serialNumber;
    private String username;
    private Double totalPrice;
    private String status;
    private String note;
    private String receiveName;
    private String receiveAddress;
    private String receivePhone;
    private LocalDate createdAt;
    private LocalDate receivedAt;


}
