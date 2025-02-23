package com.ra.service.orderDetail;

import com.ra.model.dto.response.OrderDetailResponseDto;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetailResponseDto> getOrderDetailByOrderId(Long orderId);

}
