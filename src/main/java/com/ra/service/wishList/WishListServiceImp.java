package com.ra.service.wishList;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.WishListRequestDto;
import com.ra.model.dto.response.WishListResponseDto;
import com.ra.model.entity.Product;
import com.ra.model.entity.User;
import com.ra.model.entity.WishList;
import com.ra.repository.ProductRepository;
import com.ra.repository.UserRepository;
import com.ra.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishListServiceImp implements WishListService {
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void addProductToWishList(Long userId, WishListRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Không tìm thấy người dùng"));
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new CustomException("Không tìm thấy sản phẩm"));
        WishList wishList = wishListRepository.findByUserAndProduct(user, product);
        if (wishList != null) {
            throw new CustomException("Sản phẩm đã có trong danh sách yêu thích");
        } else {
            WishList newWishList = WishList.builder()
                    .user(userRepository.findById(userId)
                            .orElseThrow(() -> new CustomException("Không tìm thấy người dùng")))
                    .product(productRepository.findById(
                                    requestDto.getProductId())
                            .orElseThrow(() -> new CustomException("Không tìm thấy sản phẩm"))
                    )
                    .build();
            wishListRepository.save(newWishList);
        }
    }

    @Override
    public List<WishListResponseDto> getWishList(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Không tìm thấy người dùng"));

        return wishListRepository.findByUser(user).stream()
                .map(this::toWishListDto)
                .toList();
    }

    @Override
    public void removeProductFromWishList(Long userId, Long wishListId) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(()->new CustomException("Không tìm thấy wishList"));
        wishListRepository.delete(wishList);
    }

    private WishListResponseDto toWishListDto(WishList wishList) {
        return WishListResponseDto.builder()
                .id(wishList.getId())
                .productId(wishList.getProduct().getId())
                .productName(wishList.getProduct().getProductName())
                .build();
    }
}
