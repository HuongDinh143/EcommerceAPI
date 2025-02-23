package com.ra.service.wishList;

import com.ra.model.dto.request.WishListRequestDto;
import com.ra.model.dto.response.WishListResponseDto;
import com.ra.model.entity.Product;

import java.util.List;

public interface WishListService {
    void addProductToWishList(Long userId, WishListRequestDto requestDto);
    List<WishListResponseDto> getWishList(Long userId);
    void removeProductFromWishList(Long userId, Long productId);
}
