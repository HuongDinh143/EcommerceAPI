package com.ra.controller;

import com.ra.exception.CustomException;
import com.ra.model.dto.request.CartItemRequest;
import com.ra.model.dto.response.ProductCartResponseDto;
import com.ra.model.entity.CartItem;
import com.ra.model.entity.User;
import com.ra.repository.UserRepository;
import com.ra.security.UserPrinciple;
import com.ra.service.cart.CartService;
import com.ra.service.product.ProductService;
import com.ra.service.shoppingCart.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartService cartService;
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


    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrinciple userPrinciple = (UserPrinciple) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        return userRepository.findByUsername(userPrinciple.getUsername())
                .map(User::getId)
                .orElseThrow(() -> new CustomException("User not found"));
    }

}
