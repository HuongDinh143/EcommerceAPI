package com.ra.service.order;

import com.ra.exception.CustomException;
import com.ra.exception.OrderCheckException;
import com.ra.exception.OrderStatusException;
import com.ra.model.dto.request.OrderRequestDto;
import com.ra.model.dto.response.OrderDetailResponseDto;
import com.ra.model.dto.response.OrderHistoryResponseDto;
import com.ra.model.dto.response.OrderResponseDto;
import com.ra.model.entity.Order;
import com.ra.model.entity.OrderDetail;
import com.ra.model.entity.Product;
import com.ra.repository.OrderRepository;
import com.ra.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

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
                .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng"));
        return toDto(order);

    }

    @Override
    public OrderResponseDto updateOrderStatus(Long id, OrderRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new CustomException("Không tìm thấy đơn hàng"));

        Order.Status currentStatus = order.getStatus();
        Order.Status newStatus;

        try {
            newStatus = Order.Status.valueOf(requestDto.getStatus().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new OrderStatusException("Trạng thái không hợp lệ: " + requestDto.getStatus());
        }

        if (newStatus == Order.Status.CANCEL && currentStatus != Order.Status.WAITING && currentStatus != Order.Status.CONFIRM) {
            throw new OrderStatusException(
                    "Chỉ có thể hủy khi đơn còn ở trạng thái đợi");
        }

        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new OrderStatusException("Chuyển đổi không hợp lệ từ " + currentStatus + " to " + newStatus);
        }


        if (newStatus == Order.Status.SUCCESS) {
            updateProductTotalSold(order);
        }

        if (newStatus == Order.Status.CANCEL) {
            restoreStock(order);
        }

        order.setStatus(newStatus);
        orderRepository.save(order);

        return toDto(order);
    }


    /**
     * Cập nhật tổng số lượng sản phẩm đã bán khi đơn hàng SUCCESS
     */
    private void updateProductTotalSold(Order order) {
        for (OrderDetail item : order.getOrderDetails()) {
            Product product = item.getProduct();
            product.setTotalSold(product.getTotalSold() + item.getOrderQuantity());
            productRepository.save(product);
        }
    }

    /**
     * Hoàn lại số lượng tồn kho khi đơn hàng bị CANCEL
     */
    private void restoreStock(Order order) {
        for (OrderDetail item : order.getOrderDetails()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getOrderQuantity());
            productRepository.save(product);
        }
    }

    /**
     * Kiểm tra trạng thái hợp lệ (chỉ cho phép tiến về phía trước)
     */
    private boolean isValidStatusTransition(Order.Status currentStatus, Order.Status newStatus) {
        return switch (currentStatus) {
            case WAITING -> newStatus == Order.Status.CONFIRM || newStatus == Order.Status.CANCEL;
            case CONFIRM -> newStatus == Order.Status.DELIVERY;
            case DELIVERY -> newStatus == Order.Status.SUCCESS;
            case SUCCESS, CANCEL -> false; // Không thể cập nhật từ SUCCESS hoặc CANCEL
        };
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
        return orderRepository.findBySerialNumberAndUserId(serialNumber, userId)
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
        Order order = orderRepository.findByIdAndUserId(orderId, userId);
        if (order != null && order.getStatus().equals(Order.Status.WAITING)) {
            order.setStatus(Order.Status.CANCEL);
            orderRepository.save(order);
        } else if (order != null && order.getStatus().equals(Order.Status.CANCEL)) {
            throw new CustomException("Đơn hàng đã hủy");
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
