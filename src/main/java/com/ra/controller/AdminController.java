package com.ra.controller;

import com.ra.model.dto.UserPermissionDto;
import com.ra.model.dto.request.CategoryRequestDto;
import com.ra.model.dto.request.CategoryUpdateRequestDto;
import com.ra.model.dto.request.OrderRequestDto;
import com.ra.model.dto.request.ProductRequestDto;
import com.ra.model.dto.response.*;
import com.ra.model.entity.Order;
import com.ra.model.entity.Role;
import com.ra.service.auth.AuthService;
import com.ra.service.category.CategoryService;
import com.ra.service.order.OrderService;
import com.ra.service.product.ProductService;
import com.ra.service.report.ReportService;
import com.ra.service.role.RoleService;
import com.ra.service.user.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private ReportService reportService;

    @GetMapping("/users")
    public ApiResponse<?> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "5") int limit,
            @RequestParam(name = "sortBy", defaultValue = "username") String sortBy,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy
    ) {
        Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<UserResponseDto> responseDtos = userService.pagination(pageable);
        return new ApiResponse<>(200, "Danh sach nguoi dung", responseDtos);

    }

    @PatchMapping("users/{id}")
    public ApiResponse<?> updatePermissionUser(@RequestBody @Valid UserPermissionDto userPermissionDTO, @PathVariable String id) {
        UserResponseDto userResponseDto = authService.updatePermission(userPermissionDTO, Long.valueOf(id));

        return new ApiResponse<>(200, "Them quyen thanh cong", userResponseDto);
    }

    @PatchMapping("/users/{id}/remove-role")
    public ApiResponse<?> removePermissionUser(
            @RequestBody @Valid UserPermissionDto userPermissionDTO,
            @PathVariable Long id) {

        UserResponseDto userResponseDto = authService.removePermission(userPermissionDTO, id);
        return new ApiResponse<>(200, "xoa quyen thanh cong", userResponseDto);
    }

    @PutMapping("/users/{userId}")
    public ApiResponse<?> updateStatusUser(@PathVariable Long userId) {
        UserResponseDto userResponseDto = userService.updateStatusUser(userId);
        if (userResponseDto.getStatus()) {
            return new ApiResponse<>(200, "Mo khoa thanh cong", userResponseDto);
        } else {
            return new ApiResponse<>(200, "Khoa thanh cong", userResponseDto);
        }

    }

    @GetMapping("/roles")
    public ApiResponse<?> getAllRoles() {
        List<Role> roles = roleService.getRoles();
        return new ApiResponse<>(200, "Danh sach quyen", roles);
    }

    @GetMapping("/users/{search}")
    public ApiResponse<List<UserResponseDto>> searchUsers(@PathVariable String search) {
        List<UserResponseDto> users = userService.findByName(search);
        return new ApiResponse<>(200, "Tim nguoi dung theo ten", users);
    }

    @GetMapping("/products")
    public ApiResponse<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy

    ) {
        Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(sortBy)
                .ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<ProductResponseDto> responseDtos = productService.pagination(pageable);
        return new ApiResponse<>(200, "Danh sach san pham", responseDtos);
    }

    @GetMapping("/products/{id}")
    public ApiResponse<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto product = productService.getProductById(id);
        return new ApiResponse<>(200, "Lay ve san pham theo id", product);
    }

    @PostMapping("products")
    public ApiResponse<ProductResponseDto> createProduct(@ModelAttribute ProductRequestDto requestDto) {
        ProductResponseDto productResponseDto = productService.addNewProduct(requestDto);
        return new ApiResponse<>(200, "Them san pham thanh cong", productResponseDto);
    }

    @PutMapping("/products/{id}")
    public ApiResponse<ProductResponseDto> updateProduct(@PathVariable Long id, @ModelAttribute ProductRequestDto requestDto) {
        ProductResponseDto productResponseDto = productService.updateProduct(id, requestDto);
        return new ApiResponse<>(200, "Update san pham thanh cong", productResponseDto);
    }

    @DeleteMapping("/products/{id}")
    public ApiResponse<?> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ApiResponse<>(200, "Xoa san pham thanh cong", id);
    }

    @GetMapping("/categories")
    public ApiResponse<Page<CategoryResponseDto>> getAllCategories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "5") int limit,
            @RequestParam(value = "sortBy", defaultValue = "catName") String sortBy,
            @RequestParam(value = "orderBy", defaultValue = "asc") String orderBy
    ) {
        Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<CategoryResponseDto> responseDtos = categoryService.findAll(pageable);
        return new ApiResponse<>(200, "Danh sach danh muc", responseDtos);
    }

    @GetMapping("/categories/{id}")
    public ApiResponse<CategoryResponseDto> getCategoryById(@PathVariable Long id) {
        CategoryResponseDto responseDto = categoryService.findById(id);
        return new ApiResponse<>(200, "Lay ve danh muc theo id", responseDto);
    }

    @PostMapping("/categories")
    public ApiResponse<CategoryResponseDto> create(@Valid @RequestBody CategoryRequestDto requestDto) {
        CategoryResponseDto responseDto = categoryService.create(requestDto);
        return new ApiResponse<>(200, "Them moi danh muc thanh cong", responseDto);
    }

    @PutMapping("/categories/{id}")
    public ApiResponse<CategoryResponseDto> update(@PathVariable Long id, @RequestBody CategoryUpdateRequestDto requestDto) {
        CategoryResponseDto responseDto = categoryService.update(id, requestDto);
        return new ApiResponse<>(200, "Update danh muc thanh cong", responseDto);
    }

    @DeleteMapping("/categories/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return new ApiResponse<>(200, "Xoa danh muc thanh cong", id);
    }

    @GetMapping("/orders")
    public ApiResponse<List<OrderResponseDto>> getAllOrders() {
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return new ApiResponse<>(200, "Danh sach don hang", orders);
    }

    @GetMapping("/orders/status/{status}")
    public ApiResponse<?> getOrderStatus(@PathVariable String status) {
        try {
            List<OrderResponseDto> orderResponseDtos = orderService.findAllByStatus(status);
            return new ApiResponse<>(200, "Lay danh sach don hang theo trang thai", orderResponseDtos);
        } catch (IllegalArgumentException e) {
            return new ApiResponse<>(401, "Trang thai don hang khong hop le", status);
        }
    }

    @GetMapping("/orders/{id}")
    public ApiResponse<OrderResponseDto> getOrderById(@PathVariable Long id) {
        OrderResponseDto orderResponseDto = orderService.findById(id);
        return new ApiResponse<>(200, "Lay don hang theo id", orderResponseDto);
    }

    @PutMapping("/orders/{id}")
    public ApiResponse<OrderResponseDto> updateOrderStatus(@PathVariable Long id, @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto orderResponseDto = orderService.updateOrderStatus(id, requestDto);
        return new ApiResponse<>(200, "Update trang thai don hang thanh cong", orderResponseDto);
    }

    @GetMapping("/orders/sales-revenue-over-time")
    public ApiResponse<Double> getSalesRevenueOverTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        double revenue = orderService.salesRevenueOverTime(Order.Status.SUCCESS, from, to);

        String message = String.format("Doanh thu bán hàng theo thời gian từ %s đến %s", from, to);

        return new ApiResponse<>(200, message, revenue);
    }

    @GetMapping("/reports/best-seller-products")
    public ApiResponse<?> getBestSellerProducts(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {
        List<BestSellerProductDto> bestSellers = reportService.getBestSellerProducts(from, to);
        String message = String.format("Danh sach san pham ban chay tu %s den %s", from, to);
        return new ApiResponse<>(200, message, bestSellers);
    }

    @GetMapping("/reports/most-liked-products")
    public ApiResponse<?> getMostLikedProducts(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        List<MostLikedProductDto> mostLikedProducts = reportService.getMostLikedProducts(from, to);
        String message = String.format("Danh sach san pham yeu thich tu %s den %s", from, to);
        return new ApiResponse<>(200, message, mostLikedProducts);
    }

    @GetMapping("/reports/revenue-by-category")
    public ApiResponse<?> getRevenueByCategory(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        List<RevenueByCategoryDto> revenueByCategory = reportService.getRevenueByCategory(from, to);
        String message = String.format("Doanh thu theo danh muc tu %s den %s", from, to);
        return new ApiResponse<>(200, message, revenueByCategory);
    }

    @GetMapping("/reports/top-spending-customers")
    public ApiResponse<?> getTopSpendingCustomers(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ) {
        List<TopSpendingResponseDto> topSpending = reportService.getTopSpending(from, to);
        String message = String.format("Tops khach hang mua hang nhieu nhat tu %s den %s", from, to);
        return new ApiResponse<>(200, message, topSpending);
    }

    @GetMapping("/reports/new-accounts-this-month")
    public ApiResponse<?> getNewAccountsThisMonth() {
        List<UserResponseDto> newAccounts = reportService.getNewAccountsThisMonth();
        return new ApiResponse<>(200, "New accounts this month", newAccounts);
    }

    @GetMapping("reports/invoices-over-time")
    public ApiResponse<?> getInvoicesOverTime(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        long invoiceCount = reportService.countInvoicesOverTime(from, to);
        String message = String.format("Thống kê số lượng hóa đơn bán ra tu %s den %s", from, to);
        return new ApiResponse<>(200, message, invoiceCount);
    }


}
