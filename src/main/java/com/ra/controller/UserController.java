package com.ra.controller;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.*;
import com.ra.model.dto.response.*;
import com.ra.model.entity.Address;
import com.ra.model.entity.CartItem;
import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import com.ra.security.UserPrinciple;
import com.ra.service.address.AddressService;
import com.ra.service.cart.CartService;
import com.ra.service.order.OrderService;
import com.ra.service.product.ProductService;
import com.ra.service.shoppingCart.ShoppingCartService;
import com.ra.service.user.UserService;
import com.ra.service.wishList.WishListService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
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
    public ResponseEntity<List<ProductCartResponseDto>> getProductsInCart() {
        Long userId = getCurrentUserId();
        List<ProductCartResponseDto> products = productService.getProductsInCart(userId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(@RequestBody CartItemRequest request, Principal principal){
        Long userId = getCurrentUserId();
        cartService.addToCart(userId,request.getProductId(),request.getQuantity());
        return ResponseEntity.ok("Đã thêm sản phẩm vào giỏ hàng!");
    }
    @PutMapping("/cart/items/{cartItemId}")
    public ResponseEntity<?> updateCartItem(@RequestBody CartItemRequest request, @PathVariable Long cartItemId) {
        Long userId = getCurrentUserId();
        cartService.updateCart(userId,cartItemId,request);
        return ResponseEntity.ok("Đã thay đổi số lượng sản phẩm");
    }
    @DeleteMapping("/cart/items/{cartItemId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long cartItemId) {
        Long userId = getCurrentUserId();
        cartService.deleteCart(userId,cartItemId);
        return ResponseEntity.ok("Đã xóa sản phẩm");
    }
    @DeleteMapping("/cart/clear")
    public ResponseEntity<?> clearCart() {
        Long userId = getCurrentUserId();
        cartService.deleteAllCarts(userId);
        return ResponseEntity.ok("Đã xóa toàn bộ sản phẩm");
    }
    @PostMapping("/cart/checkout")
    public ResponseEntity<?> checkout(@RequestBody OrderRequestDto requestDto) {
        Long userId = getCurrentUserId();
        cartService.checkoutCart(userId,requestDto);
        return ResponseEntity.ok("Đặt hàng thành công");
    }
    @GetMapping("/account")
    public ResponseEntity<UserResponseDto> getCurrentUser() {
        Long userId = getCurrentUserId();
        UserResponseDto userResponseDto = userService.findById(userId);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
    @PutMapping("/account")
    public ResponseEntity<?> updateCurrentUser(@Valid @RequestBody UserUpdateRequestDto requestDto) {
        Long userId = getCurrentUserId();
        UserResponseDto userUpdate = userService.updateUser(userId,requestDto);
        return new ResponseEntity<>(userUpdate, HttpStatus.OK);
    }
    @PostMapping("/account/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto) {
        Long userId = getCurrentUserId();
        userService.changePassword(userId, requestDto);
        return ResponseEntity.ok("Thay đổi mật khẩu thành công");
    }
    @DeleteMapping("account/addresses/{addressId}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long addressId){
        Long userId = getCurrentUserId();
        addressService.deleteAddressById(addressId,userId);
        return ResponseEntity.ok("Xóa địa chỉ thành công");
    }
    @GetMapping("/account/addresses")
    public ResponseEntity<List<AddressResponseDto>> getCurrentUserAddress() {
        Long userId = getCurrentUserId();
        List<AddressResponseDto> addressList = addressService.getAddressByUserId(userId);
        return new ResponseEntity<>(addressList, HttpStatus.OK);
    }
    @GetMapping("/account/addresses/{addressId}")
    public ResponseEntity<AddressResponseDto> getCurrentUserAddressById(@PathVariable Long addressId) {
        Long userId = getCurrentUserId();
        AddressResponseDto addressResponseDto = addressService.getAddressById(addressId,userId);
        return new ResponseEntity<>(addressResponseDto, HttpStatus.OK);
    }
    @GetMapping("/history")
    public ResponseEntity<List<OrderHistoryResponseDto>> getCurrentUserHistory() {
        Long userId = getCurrentUserId();
        List<OrderHistoryResponseDto> listOrders = orderService.getOrderHistory(userId);
        return new ResponseEntity<>(listOrders, HttpStatus.OK);
    }
    @GetMapping("/history/serial/{serialNumber}")
    public ResponseEntity<List<OrderDetailResponseDto>> getCurrentUserHistoryBySerialNumber(@PathVariable String serialNumber) {
        Long userId = getCurrentUserId();
        List<OrderDetailResponseDto> orderDetailResponseDtos = orderService.getOrderDetailBySerialNumber(serialNumber,userId);
        return new ResponseEntity<>(orderDetailResponseDtos, HttpStatus.OK);
    }
    @GetMapping("/history/status/{orderStatus}")
    public ResponseEntity<List<OrderHistoryResponseDto>> getCurrentUserHistoryByOrderStatus(@PathVariable String orderStatus) {
        Long userId = getCurrentUserId();
        List<OrderHistoryResponseDto> listOrders = orderService.getOrderHistoryByStatus(orderStatus,userId);
        return new ResponseEntity<>(listOrders, HttpStatus.OK);
    }
    @PutMapping("/history/{orderId}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable Long orderId) {
        Long userId = getCurrentUserId();
        orderService.cancelOrderByStatusWaiting(orderId,userId);
        return ResponseEntity.ok("Hủy đơn hàng thành công");
    }
    @PostMapping("/wish-list")
    public ResponseEntity<?> addProductToWishList(@RequestBody WishListRequestDto requestDto) {
        Long userId = getCurrentUserId();
        wishListService.addProductToWishList(userId,requestDto);
        return ResponseEntity.ok("Thêm vào danh sách yêu thích thành công");
    }
    @GetMapping("/wish-list")
    public ResponseEntity<List<WishListResponseDto>> getCurrentUserWishList() {
        Long userId = getCurrentUserId();
        List<WishListResponseDto> wishList = wishListService.getWishList(userId);
        return new ResponseEntity<>(wishList, HttpStatus.OK);
    }
    @DeleteMapping("/wish-list/{productId}")
    public ResponseEntity<?> deleteProductFromWishList(@PathVariable Long productId) {
        Long userId = getCurrentUserId();
        wishListService.removeProductFromWishList(userId,productId);
        return ResponseEntity.ok("Xóa thành công sản phẩm khỏi danh sách yêu thích");
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
