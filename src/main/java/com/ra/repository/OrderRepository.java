package com.ra.repository;

import com.ra.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus(Order.Status status);
    List<Order> findByCreatedAtBetween(LocalDate from, LocalDate to);

}
