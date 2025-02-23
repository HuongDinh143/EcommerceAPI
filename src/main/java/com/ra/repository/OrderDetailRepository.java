package com.ra.repository;

import com.ra.model.entity.Order;
import com.ra.model.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findTop10ByOrderByOrderQuantityDesc();
    List<OrderDetail> findByOrder_CreatedAtBetween(LocalDate from, LocalDate to);
    List<OrderDetail> findByOrder(Order order);
}
