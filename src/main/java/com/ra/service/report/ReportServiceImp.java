package com.ra.service.report;

import com.ra.model.dto.response.*;
import com.ra.model.entity.*;
import com.ra.repository.OrderDetailRepository;
import com.ra.repository.OrderRepository;
import com.ra.repository.UserRepository;
import com.ra.repository.WishListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportServiceImp implements ReportService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private WishListRepository wishListRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<BestSellerProductDto> getBestSellerProducts(LocalDate from, LocalDate to) {
        List<OrderDetail> orderDetails = orderDetailRepository.findByOrder_CreatedAtBetween(from, to);
        Map<Long,BestSellerProductDto> productSalesMap = new HashMap<>();
        for (OrderDetail od : orderDetails) {
            Long productId = od.getProduct().getId();
            String productName = od.getProductName();
            Long quantity = od.getOrderQuantity();
            productSalesMap.putIfAbsent(productId, new BestSellerProductDto(productId, productName, 0L));
            productSalesMap.get(productId).setTotalSold(productSalesMap.get(productId).getTotalSold() + quantity);
        }
        return productSalesMap.values().stream()
                .sorted((a, b) -> Long.compare(b.getTotalSold(), a.getTotalSold())) // Sắp xếp giảm dần
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<MostLikedProductDto> getMostLikedProducts(LocalDate from, LocalDate to) {
        List<WishList> wishLists = wishListRepository.findByProduct_CreatedAtBetween(from, to);
        Map<Product, Long> productCount = wishLists.stream()
                .collect(Collectors.groupingBy(WishList::getProduct, Collectors.counting()));

        return productCount.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .limit(10)
                .map(entry -> new MostLikedProductDto(
                        entry.getKey().getId(),
                        entry.getKey().getProductName(),
                        entry.getValue()))
                .collect(Collectors.toList());
    }
    @Override
    public List<RevenueByCategoryDto> getRevenueByCategory(LocalDate from, LocalDate to) {
        List<Order> orders = orderRepository.findByCreatedAtBetween(from, to);

        Map<Category, Double> revenueByCategory = orders.stream()
                .flatMap(order -> order.getOrderDetails().stream())
                .collect(Collectors.groupingBy(
                        orderDetail -> orderDetail.getProduct().getCategory(),
                        Collectors.summingDouble(orderDetail -> orderDetail.getOrderQuantity() * orderDetail.getProduct().getUnitPrice()) // Tính tổng doanh thu
                ));

        return revenueByCategory.entrySet().stream()
                .map(entry -> new RevenueByCategoryDto(entry.getKey().getId(), entry.getKey().getCatName(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TopSpendingResponseDto> getTopSpending(LocalDate from, LocalDate to) {
        List<Order> orders = orderRepository.findByCreatedAtBetween(from, to);
        Map<Long,TopSpendingResponseDto> userMap = new HashMap<>();
        for (Order order : orders) {
            Long userId = order.getUser().getId();
            String username = order.getUser().getUsername();
            double totalPrice = order.getTotalPrice();
            userMap.putIfAbsent(userId, new TopSpendingResponseDto(userId, username, (double) 0L));
            userMap.get(userId).setTotalPrice(userMap.get(userId).getTotalPrice() + totalPrice);

        }
        return userMap.values().stream()
                .sorted((a, b) -> Double.compare(b.getTotalPrice(), a.getTotalPrice()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDto> getNewAccountsThisMonth() {
        List<User> users = userRepository.findAll();
        LocalDate firstDayOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate lastDayOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());


        return users.stream()
                .filter(user -> {
                    LocalDate createdAt = user.getCreatedAt();
                    return !createdAt.isBefore(firstDayOfMonth) && !createdAt.isAfter(lastDayOfMonth);
                }).map(user -> UserResponseDto.builder()
                        .id(user.getId())
                        .username(user.getUsername())
                        .fullName(user.getFullName())
                        .address(user.getAddress())
                        .email(user.getEmail())
                        .phone(user.getPhone())
                        .roles(user.getRoles())
                        .createdAt(firstDayOfMonth)
                        .build()).collect(Collectors.toList());

    }

    @Override
    public Long countInvoicesOverTime(LocalDate from, LocalDate to) {
        List<Order> orders = orderRepository.findByCreatedAtBetween(from, to);
        return (long) orders.size();
    }


}
