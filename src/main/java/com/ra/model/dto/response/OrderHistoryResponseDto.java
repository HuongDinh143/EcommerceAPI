package com.ra.model.dto.response;

import com.ra.model.entity.OrderDetail;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderHistoryResponseDto {
    private Long id;
    private LocalDate orderDate;
    private Double totalPrice;
    private String status;
    private List<OrderDetailResponseDto> orderDetails;

}
