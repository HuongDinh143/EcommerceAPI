package com.ra.service.cart;

import com.ra.exception.CustomException;
import com.ra.exception.OrderCheckException;
import com.ra.model.dto.request.CartItemRequest;
import com.ra.model.dto.request.OrderRequestDto;
import com.ra.model.entity.*;
import com.ra.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.View;

import java.time.LocalDate;
import java.util.*;

@Service
public class CartServiceImp implements CartService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private View error;

    @Override
    public void addToCart(Long userId, Long productId, Long quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User không tồn tại"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException("Product không tồn tại"));
        if (quantity > product.getStockQuantity()) {
            throw new CustomException("Số lượng hàng trong kho không đủ. Chỉ có thể thêm tối đa " + product.getStockQuantity() + " sản phẩm.");
        }

        ShoppingCart shoppingCart = shoppingCartRepository.findByUser(user)
                .orElseGet(() -> {
                    ShoppingCart newCart = new ShoppingCart();
                    newCart.setUser(user);
                    return shoppingCartRepository.save(newCart);
                });

        Optional<CartItem> existingCartItem = cartItemRepository.findByShoppingCartAndProduct(shoppingCart, product);

        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem newCartItem = new CartItem();
            newCartItem.setShoppingCart(shoppingCart);
            newCartItem.setUser(user);
            newCartItem.setProduct(product);
            newCartItem.setQuantity(quantity);
            cartItemRepository.save(newCartItem);
            shoppingCart.getItems().add(newCartItem);
        }
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public void updateCart(Long userId, Long cartItemId, CartItemRequest request) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CustomException("cart item not found"));
        Product product = productRepository.findById(cartItem.getProduct().getId())
                .orElseThrow(() -> new CustomException("product not found"));
        if (request.getQuantity() > product.getStockQuantity()) {
            throw new OrderCheckException("Số lượng hàng trong kho không đủ. Chỉ có thể thêm tối đa " + product.getStockQuantity() + " sản phẩm.", new HashMap<>());
        }
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteCart(Long userId, Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CustomException("không tìm thấy cartItem"));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void deleteAllCarts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("user not found"));
        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        cartItemRepository.deleteAll(cartItems);
    }

    @Override
    @Transactional
    public void checkoutCart(Long userId, OrderRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("Không tìm thấy người dùng"));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new CustomException("Giỏ hàng rỗng");
        }

        double totalPrice = 0.0;
        List<OrderDetail> orderDetails = new ArrayList<>();
        Map<String, String> errors = new HashMap<>();

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new CustomException("Không tìm thấy sản phẩm"));

            if (product.getStockQuantity() < cartItem.getQuantity()) {
                errors.put(product.getProductName(), "Không đủ hàng, chỉ còn " + product.getStockQuantity());
            }
        }

        if (!errors.isEmpty()) {
            throw new OrderCheckException("Có sản phẩm không đủ hàng", errors);
        }

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getId()).get();

            double itemTotal = cartItem.getQuantity() * product.getUnitPrice();
            totalPrice += itemTotal;

            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProduct(product);
            orderDetail.setProductName(product.getProductName());
            orderDetail.setUnitPrice(product.getUnitPrice());
            orderDetail.setOrderQuantity(cartItem.getQuantity());
            orderDetails.add(orderDetail);
        }

        // Tạo đơn hàng
        Order order = Order.builder()
                .serialNumber(UUID.randomUUID().toString())
                .user(user)
                .receiveAddress(requestDto.getReceiveAddress())
                .receiveName(requestDto.getReceiveName())
                .receivePhone(requestDto.getReceivePhone())
                .status(Order.Status.WAITING)
                .createdAt(LocalDate.now())
                .note(requestDto.getNote())
                .totalPrice(totalPrice)
                .build();

        order = orderRepository.save(order);

        for (OrderDetail orderDetail : orderDetails) {
            orderDetail.setOrder(order);
        }

        orderDetailRepository.saveAll(orderDetails);

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProduct().getId()).get();
            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepository.save(product);
        }

        cartItemRepository.deleteAll(cartItems);
    }


}
