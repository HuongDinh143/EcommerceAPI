package com.ra.repository;

import com.ra.model.dto.ProductResponseDto;
import com.ra.model.entity.Category;
import com.ra.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductName(String productName);
    List<Product> findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String productName, String description);
    List<Product> findByIsFeaturedTrue();
    @Query("""
        SELECT p FROM Product p 
        LEFT JOIN p.orderDetails od 
        GROUP BY p 
        ORDER BY COUNT(od) DESC
    """)
    List<Product> findTop10ByOrderDetails();
    List<Product> findTop10ByOrderByCreatedAtDesc();
    List<Product> findByCategory(Category category);


}
