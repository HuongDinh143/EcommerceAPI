package com.ra.repository;

import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductName(String productName);
    List<Product> findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String productName, String description);
    List<Product> findByIsFeaturedTrue();
    List<Product> findTop10ByOrderByCreatedAtDesc();
    List<Product> findByCategory(Category category);
    List<Product> findByCreatedAtBetween(LocalDate startDate, LocalDate endDate);



}
