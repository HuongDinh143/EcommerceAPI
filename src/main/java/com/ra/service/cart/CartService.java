package com.ra.service.cart;

import com.ra.model.dto.request.CartItemRequest;
import com.ra.model.dto.request.OrderRequestDto;
import com.ra.model.dto.response.CartItemResponseDto;

import java.util.List;

public interface CartService {
    void addToCart(Long userId, Long productId, Long quantity);
    void updateCart(Long userId, Long cartItemId, CartItemRequest request);
    void deleteCart(Long userId, Long cartItemId);
    void deleteAllCarts(Long userId);
    void checkoutCart(Long userId, OrderRequestDto requestDto);
}
