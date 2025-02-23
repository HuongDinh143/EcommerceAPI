package com.ra.service.orderDetail;

import com.ra.model.dto.response.OrderDetailResponseDto;
import com.ra.model.entity.Order;
import com.ra.model.entity.OrderDetail;
import com.ra.repository.OrderDetailRepository;
import com.ra.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailServiceImp implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Override
    public List<OrderDetailResponseDto> getOrderDetailByOrderId(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (Order order : orders) {
            List<OrderDetail> orderDetails = order.getOrderDetails();
            orderDetailList.addAll(orderDetails);
        }
        return orderDetailList.stream().map(this::toDto).toList();
    }
    private OrderDetailResponseDto toDto(OrderDetail orderDetail) {
        return OrderDetailResponseDto.builder()
                .id(orderDetail.getId())
                .productName(orderDetail.getProductName())
                .orderQuantity(orderDetail.getOrderQuantity())
                .orderQuantity(orderDetail.getOrderQuantity())
                .totalPrice(orderDetail.getOrderQuantity()*orderDetail.getUnitPrice())
                .build();

    }
}
