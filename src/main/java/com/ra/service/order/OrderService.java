package com.ra.service.order;

import com.ra.model.dto.request.OrderRequestDto;
import com.ra.model.dto.response.OrderResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<OrderResponseDto> getAllOrders();
    List<OrderResponseDto> findAllByStatus(String status);
    OrderResponseDto findById(long id);
    OrderResponseDto updateOrderStatus(Long id,OrderRequestDto requestDto);
    double findByCreatedAtBetween(LocalDate from, LocalDate to);
}
