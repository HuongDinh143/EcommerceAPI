package com.ra.service.order;

import com.ra.exception.CustomException;
import com.ra.exception.OrderStatusException;
import com.ra.model.dto.request.OrderRequestDto;
import com.ra.model.dto.response.OrderDetailResponseDto;
import com.ra.model.dto.response.OrderHistoryResponseDto;
import com.ra.model.dto.response.OrderResponseDto;
import com.ra.model.entity.Order;
import com.ra.model.entity.OrderDetail;
import com.ra.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
    public OrderResponseDto findById(Long id) {
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
            throw new OrderStatusException("Invalid order status: " + requestDto.getStatus());
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

    @Override
    public List<OrderHistoryResponseDto> getOrderHistory(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::toOrderDto).toList();
    }

    @Override
    public List<OrderDetailResponseDto> getOrderDetailBySerialNumber(String serialNumber, Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .filter(order -> serialNumber.equals(order.getSerialNumber()))
                .findFirst()
                .map(order -> order.getOrderDetails().stream()
                        .map(this::toOrderDetailDto)
                        .toList())
                .orElse(Collections.emptyList());
    }

    @Override
    public List<OrderHistoryResponseDto> getOrderHistoryByStatus(String status, Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .filter(order -> Objects.equals(order.getStatus().name(), status))
                .map(this::toOrderDto)
                .toList();
    }

    @Override
    public void cancelOrderByStatusWaiting(Long orderId, Long userId) {
        Order order = orderRepository.findByIdAndUserId(orderId,userId);
        if (order!=null && order.getStatus().equals(Order.Status.WAITING)) {
            order.setStatus(Order.Status.CANCEL);
            orderRepository.save(order);
        }else {
            throw new CustomException("Không thể hủy đơn hàng");
        }
    }


    private OrderHistoryResponseDto toOrderDto(Order order) {
        List<OrderDetail> orderDetails = order.getOrderDetails();
        return OrderHistoryResponseDto.builder()
                .id(order.getId())
                .orderDate(order.getCreatedAt())
                .status(order.getStatus().name())
                .totalPrice(order.getTotalPrice())
                .orderDetails(orderDetails.stream()
                        .map(this::toOrderDetailDto)
                        .collect(Collectors.toList()))
                .build();
    }

    private OrderDetailResponseDto toOrderDetailDto(OrderDetail orderDetail) {
        return OrderDetailResponseDto.builder()
                .id(orderDetail.getId())
                .productName(orderDetail.getProductName())
                .unitPrice(orderDetail.getUnitPrice())
                .orderQuantity(orderDetail.getOrderQuantity())
                .totalPrice(orderDetail.getUnitPrice() != null ?
                        orderDetail.getOrderQuantity() * orderDetail.getUnitPrice() : 0)
                .build();
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
