package com.ra.service.order;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.OrderRequestDto;
import com.ra.model.dto.response.OrderResponseDto;
import com.ra.model.entity.Order;
import com.ra.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public List<OrderResponseDto> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream().map(this::toDto).toList();
    }

    @Override
    public List<OrderResponseDto> findAllByStatus(String status) {
        List<Order> orders = orderRepository.findAllByStatus(Order.Status.valueOf(status));
        return orders.stream().map(this::toDto).toList();
    }

    @Override
    public OrderResponseDto findById(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new CustomException("Order not found"));
        return toDto(order);
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long id, OrderRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new CustomException("Order not found"));

        try {
            Order.Status newStatus = Order.Status.valueOf(requestDto.getStatus().toUpperCase());
            order.setStatus(newStatus);
            orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new CustomException("Invalid order status: " + requestDto.getStatus());
        }

        return toDto(order);
    }

    @Override
    public double findByCreatedAtBetween(LocalDate from, LocalDate to) {
        return orderRepository.findByCreatedAtBetween(from, to)
                .stream()
                .mapToDouble(Order::getTotalPrice)
                .sum();
    }

    private OrderResponseDto toDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .serialNumber(order.getSerialNumber())
                .username(order.getUser().getUsername())
                .status(order.getStatus().name())
                .note(order.getNote())
                .totalPrice(order.getTotalPrice())
                .receiveName(order.getReceiveName())
                .receiveAddress(order.getReceiveAddress())
                .receivePhone(order.getReceivePhone())
                .createdAt(order.getCreatedAt())
                .receivedAt(order.getReceivedAt())
                .build();
    }
}
