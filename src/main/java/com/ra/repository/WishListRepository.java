package com.ra.repository;

import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import com.ra.model.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    List<WishList> findByProduct_CreatedAtBetween(LocalDate from, LocalDate to);
    WishList findByUserAndProduct(User user, Product product);
    List<WishList> findByUser(User user);
}
