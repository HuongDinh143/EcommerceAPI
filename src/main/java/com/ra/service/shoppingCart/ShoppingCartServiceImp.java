package com.ra.service.shoppingCart;

import com.ra.model.dto.ProductCartResponseDto;
import com.ra.model.dto.ProductResponseDto;
import com.ra.model.dto.ShoppingCartResponse;
import com.ra.model.entity.CartItem;
import com.ra.model.entity.Product;
import com.ra.model.entity.ShoppingCart;
import com.ra.model.entity.User;
import com.ra.repository.ProductRepository;
import com.ra.repository.ShoppingCartRepository;
import com.ra.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class ShoppingCartServiceImp implements ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;

}
