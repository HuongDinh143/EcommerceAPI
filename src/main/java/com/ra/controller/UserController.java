package com.ra.controller;

import com.ra.model.dto.request.*;
import com.ra.model.dto.response.*;
import com.ra.security.UserPrinciple;
import com.ra.service.address.AddressService;
import com.ra.service.cart.CartService;
import com.ra.service.order.OrderService;
import com.ra.service.product.ProductService;
import com.ra.service.user.UserService;
import com.ra.service.wishList.WishListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private CartService cartService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private WishListService wishListService;

    @GetMapping("/cart/list")
    public ApiResponse<List<ProductCartResponseDto>> getProductsInCart() {
        Long userId = getCurrentUserId();
        List<ProductCartResponseDto> products = productService.getProductsInCart(userId);
        return new ApiResponse<>(200, "Lấy danh sách giỏ hàng thành công", products);
    }

    @PostMapping("/cart/add")
    public ApiResponse<?> addToCart(@RequestBody CartItemRequest request, Principal principal) {
        Long userId = getCurrentUserId();
        cartService.addToCart(userId, request.getProductId(), request.getQuantity());
        return new ApiResponse<>(200, "Đã thêm sản phẩm vào giỏ hàng!", request);
    }

    @PutMapping("/cart/items/{cartItemId}")
    public ApiResponse<?> updateCartItem(@RequestBody CartItemRequest request, @PathVariable Long cartItemId) {
        Long userId = getCurrentUserId();
        cartService.updateCart(userId, cartItemId, request);
        return new ApiResponse<>(200, "Đã thay đổi số lượng sản phẩm", request);
    }

    @DeleteMapping("/cart/items/{cartItemId}")
    public ApiResponse<?> deleteCartItem(@PathVariable Long cartItemId) {
        Long userId = getCurrentUserId();
        cartService.deleteCart(userId, cartItemId);
        return new ApiResponse<>(200, "Đã xóa sản phẩm", cartItemId);
    }

    @DeleteMapping("/cart/clear")
    public ApiResponse<?> clearCart() {
        Long userId = getCurrentUserId();
        cartService.deleteAllCarts(userId);
        return new ApiResponse<>(200, "Đã xóa toàn bộ sản phẩm", "");
    }

    @PostMapping("/cart/checkout")
    public ApiResponse<?> checkout(@RequestBody OrderRequestDto requestDto) {
        Long userId = getCurrentUserId();
        cartService.checkoutCart(userId, requestDto);
        return new ApiResponse<>(200, "Đặt hàng thành công", requestDto);
    }

    @GetMapping("/account")
    public ApiResponse<UserResponseDto> getCurrentUser() {
        Long userId = getCurrentUserId();
        UserResponseDto userResponseDto = userService.findById(userId);
        return new ApiResponse<>(200, "Thong tin tai khoan", userResponseDto);
    }

    @PutMapping("/account")
    public ApiResponse<?> updateCurrentUser(@Valid @RequestBody UserUpdateRequestDto requestDto) {
        Long userId = getCurrentUserId();
        UserResponseDto userUpdate = userService.updateUser(userId, requestDto);
        return new ApiResponse<>(200, "Update thong tin tai khoan thanh cong", userUpdate);
    }

    @PostMapping("/account/change-password")
    public ApiResponse<String> changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto) {
        Long userId = getCurrentUserId();
        userService.changePassword(userId, requestDto);
        return new ApiResponse<>(200, "Thay đổi mật khẩu thành công", "Moi ban dang nhap lai");
    }

    @DeleteMapping("account/addresses/{addressId}")
    public ApiResponse<?> deleteAddress(@PathVariable Long addressId) {
        Long userId = getCurrentUserId();
        addressService.deleteAddressById(addressId, userId);
        return new ApiResponse<>(200, "Xóa địa chỉ thành công", addressId);
    }

    @GetMapping("/account/addresses")
    public ApiResponse<List<AddressResponseDto>> getCurrentUserAddress() {
        Long userId = getCurrentUserId();
        List<AddressResponseDto> addressList = addressService.getAddressByUserId(userId);
        return new ApiResponse<>(200, "Danh sách địa chỉ", addressList);
    }

    @GetMapping("/account/addresses/{addressId}")
    public ApiResponse<AddressResponseDto> getCurrentUserAddressById(@PathVariable Long addressId) {
        Long userId = getCurrentUserId();
        AddressResponseDto addressResponseDto = addressService.getAddressById(addressId, userId);
        return new ApiResponse<>(200, "Lấy về địa chỉ theo addressId", addressResponseDto);
    }

    @GetMapping("/history")
    public ApiResponse<List<OrderHistoryResponseDto>> getCurrentUserHistory() {
        Long userId = getCurrentUserId();
        List<OrderHistoryResponseDto> listOrders = orderService.getOrderHistory(userId);
        return new ApiResponse<>(200, "Lịch sử mua hàng", listOrders);
    }

    @GetMapping("/history/serial/{serialNumber}")
    public ApiResponse<List<OrderDetailResponseDto>> getCurrentUserHistoryBySerialNumber(@PathVariable String serialNumber) {
        Long userId = getCurrentUserId();
        List<OrderDetailResponseDto> orderDetailResponseDtos = orderService.getOrderDetailBySerialNumber(serialNumber, userId);
        return new ApiResponse<>(200, "Lịch sử mua hàng theo serialNumber", orderDetailResponseDtos);
    }

    @GetMapping("/history/status/{orderStatus}")
    public ApiResponse<List<OrderHistoryResponseDto>> getCurrentUserHistoryByOrderStatus(@PathVariable String orderStatus) {
        Long userId = getCurrentUserId();
        List<OrderHistoryResponseDto> listOrders = orderService.getOrderHistoryByStatus(orderStatus, userId);
        return new ApiResponse<>(200, "lấy ra danh sách lịch sử đơn hàng theo trạng thái đơn hàng", listOrders);
    }

    @PutMapping("/history/{orderId}/cancel")
    public ApiResponse<?> cancelOrder(@PathVariable Long orderId) {
        Long userId = getCurrentUserId();
        orderService.cancelOrderByStatusWaiting(orderId, userId);
        return new ApiResponse<>(200, "Hủy đơn hàng thành công", orderId);
    }

    @PostMapping("/wish-list")
    public ApiResponse<?> addProductToWishList(@RequestBody WishListRequestDto requestDto) {
        Long userId = getCurrentUserId();
        wishListService.addProductToWishList(userId, requestDto);
        return new ApiResponse<>(200, "Thêm vào danh sách yêu thích thành công", requestDto);
    }

    @GetMapping("/wish-list")
    public ApiResponse<List<WishListResponseDto>> getCurrentUserWishList() {
        Long userId = getCurrentUserId();
        List<WishListResponseDto> wishList = wishListService.getWishList(userId);
        return new ApiResponse<>(200, "Danh sách sản phẩm yêu thích", wishList);
    }

    @DeleteMapping("/wish-list/{productId}")
    public ApiResponse<?> deleteProductFromWishList(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        wishListService.removeProductFromWishList(userId, productId);
        return new ApiResponse<>(200,"Xóa thành công sản phẩm khỏi danh sách yêu thích", productId);
    }


    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrinciple) {
            return ((UserPrinciple) principal).getUser().getId();
        }
        return null;
    }

}
