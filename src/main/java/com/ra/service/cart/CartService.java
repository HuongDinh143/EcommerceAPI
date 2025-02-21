package com.ra.service.cart;

public interface CartService {
    void addToCart(Long userId, Long productId, Long quantity);
}
