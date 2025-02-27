package com.ra.repository;

import com.ra.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByStatus(Order.Status status);
    List<Order> findByCreatedAtBetween(LocalDate from, LocalDate to);
    List<Order> findByUserId(Long userId);
    Order findByIdAndUserId(Long id, Long userId);
    Optional<Order> findBySerialNumberAndUserId(String serialNumber, Long userId);


}
