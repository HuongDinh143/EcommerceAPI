package com.ra.controller;

import com.ra.model.dto.ProductCartResponseDto;
import com.ra.model.dto.ShoppingCartResponse;
import com.ra.model.entity.ShoppingCart;
import com.ra.model.entity.User;
import com.ra.repository.ShoppingCartRepository;
import com.ra.repository.UserRepository;
import com.ra.service.product.ProductService;
import com.ra.service.shoppingCart.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.attribute.UserPrincipal;
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
    @GetMapping("/cart/list")
    public ResponseEntity<List<ProductCartResponseDto>> getProductsInCart() {
        Long userId = getCurrentUserId();
        List<ProductCartResponseDto> products = productService.getProductsInCart(userId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userRepository.findByUsername(userPrincipal.getName())
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}
