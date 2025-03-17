package com.ra.service.order;

import com.ra.model.dto.request.OrderRequestDto;
import com.ra.model.dto.response.OrderDetailResponseDto;
import com.ra.model.dto.response.OrderHistoryResponseDto;
import com.ra.model.dto.response.OrderResponseDto;
import com.ra.model.entity.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<OrderResponseDto> getAllOrders();
    List<OrderResponseDto> findAllByStatus(String status);
    OrderResponseDto findById(Long id);
    OrderResponseDto updateOrderStatus(Long id,OrderRequestDto requestDto);
    double salesRevenueOverTime (Order.Status status, LocalDate from, LocalDate to);
    List<OrderHistoryResponseDto> getOrderHistory(Long userId);
    List<OrderDetailResponseDto> getOrderDetailBySerialNumber(String serialNumber,Long userId);
    List<OrderHistoryResponseDto> getOrderHistoryByStatus(String status,Long userId);
    void cancelOrderByStatusWaiting(Long orderId,Long userId);
}
