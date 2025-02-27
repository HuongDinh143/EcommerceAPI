package com.ra.controller;

import com.ra.model.dto.*;
import com.ra.model.dto.request.CategoryRequestDto;
import com.ra.model.dto.request.CategoryUpdateRequestDto;
import com.ra.model.dto.request.OrderRequestDto;
import com.ra.model.dto.request.ProductRequestDto;
import com.ra.model.dto.response.*;
import com.ra.model.entity.Role;
import com.ra.model.entity.User;
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
    public ResponseEntity<?> getAllUsers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "5") int limit,
            @RequestParam(name = "sortBy", defaultValue = "username") String sortBy,
            @RequestParam(name = "orderBy", defaultValue = "asc") String orderBy
    ) {
        Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<UserResponseDto> responseDtos = userService.pagination(pageable);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);

    }
    @PatchMapping("users/{id}")
    public ResponseEntity<?> updatePermissionUser(@RequestBody @Valid UserPermissionDto userPermissionDTO, @PathVariable String id) {
        UserResponseDto userResponseDto = authService.updatePermission(userPermissionDTO, Long.valueOf(id));
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
    @PatchMapping("/users/{id}/remove-role")
    public ResponseEntity<?> removePermissionUser(
            @RequestBody @Valid UserPermissionDto userPermissionDTO,
            @PathVariable Long id) {

        UserResponseDto userResponseDto = authService.removePermission(userPermissionDTO, id);
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateStatusUser(@PathVariable Long userId) {
        UserResponseDto userResponseDto= userService.updateStatusUser(userId);
        if (userResponseDto.getStatus()){
            return ResponseEntity.ok("Mở khóa thành công");
        }else{
            return ResponseEntity.ok("Khóa thành công");
        }

    }
    @GetMapping("/roles")
    public ResponseEntity<?> getAllRoles(){
        List<Role> roles = roleService.getRoles();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }
    @GetMapping("/users/{search}")
    public ResponseEntity<List<UserResponseDto>> searchUsers( @PathVariable String search) {
        List<UserResponseDto> users = userService.findByName(search);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    @GetMapping("/products")
    public ResponseEntity<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "limit",defaultValue = "10") int limit,
            @RequestParam(name = "sortBy",defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "orderBy",defaultValue = "asc") String orderBy

    ) {
        Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(sortBy)
                .ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<ProductResponseDto> responseDtos = productService.pagination(pageable);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }
    @PostMapping("products")
    public ResponseEntity<ProductResponseDto> createProduct(@ModelAttribute ProductRequestDto requestDto){
        ProductResponseDto productResponseDto = productService.addNewProduct(requestDto);
        return new ResponseEntity<>(productResponseDto, HttpStatus.CREATED);
    }
    @PutMapping("/products/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long id,@ModelAttribute ProductRequestDto requestDto){
        ProductResponseDto productResponseDto = productService.updateProduct(id,requestDto);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Xóa sản phẩm thành công");
    }
    @GetMapping("/categories")
    public ResponseEntity<Page<CategoryResponseDto>> getAllCategories(
            @RequestParam(value = "page",defaultValue = "0") int page,
            @RequestParam(value = "limit",defaultValue = "5") int limit,
            @RequestParam(value = "sortBy",defaultValue = "catName") String sortBy,
            @RequestParam(value = "orderBy",defaultValue = "asc") String orderBy
    ){
        Sort sort = orderBy.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<CategoryResponseDto> responseDtos = categoryService.findAll(pageable);
        return new ResponseEntity<>(responseDtos, HttpStatus.OK);
    }
    @GetMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseDto> getCategoryById(@PathVariable Long id){
        CategoryResponseDto responseDto = categoryService.findById(id);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    @PostMapping("/categories")
    public ResponseEntity<CategoryResponseDto> create(@Valid @RequestBody CategoryRequestDto requestDto){
        CategoryResponseDto responseDto = categoryService.create(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoryResponseDto> update(@PathVariable Long id,@RequestBody CategoryUpdateRequestDto requestDto){
        CategoryResponseDto responseDto = categoryService.update(id,requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    @DeleteMapping("/categories/{id}")
    public void delete(@PathVariable Long id){
        categoryService.delete(id);
    }
    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(){
        List<OrderResponseDto> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders,HttpStatus.OK);
    }
    @GetMapping("/orders/status/{status}")
    public ResponseEntity<?> getOrderStatus(@PathVariable String status) {
        try {
            List<OrderResponseDto> orderResponseDtos = orderService.findAllByStatus(status);
            return ResponseEntity.ok(orderResponseDtos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid order status: " + status);
        }
    }
    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id){
        OrderResponseDto orderResponseDto = orderService.findById(id);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
    }
    @PutMapping("/orders/{id}")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long id,@RequestBody OrderRequestDto requestDto){
        OrderResponseDto orderResponseDto = orderService.updateOrderStatus(id,requestDto);
        return new ResponseEntity<>(orderResponseDto, HttpStatus.OK);
    }
    @GetMapping("/orders/sales-revenue-over-time")
    public ResponseEntity<?> getSalesRevenueOverTime(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {
        double revenue = orderService.findByCreatedAtBetween(from, to);
        return new ResponseEntity<>(revenue, HttpStatus.OK);
    }
    @GetMapping("/reports/best-seller-products")
    public ResponseEntity<?> getBestSellerProducts(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {
        List<BestSellerProductDto> bestSellers = reportService.getBestSellerProducts(from, to);
        return new ResponseEntity<>(bestSellers, HttpStatus.OK);
    }
    @GetMapping("/reports/most-liked-products")
    public ResponseEntity<?> getMostLikedProducts(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        List<MostLikedProductDto> mostLikedProducts = reportService.getMostLikedProducts(from, to);
        return new ResponseEntity<>(mostLikedProducts, HttpStatus.OK);
    }
    @GetMapping("/reports/revenue-by-category")
    public ResponseEntity<?> getRevenueByCategory(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {

        List<RevenueByCategoryDto> revenueByCategory = reportService.getRevenueByCategory(from, to);
        return new ResponseEntity<>(revenueByCategory, HttpStatus.OK);
    }
    @GetMapping("/reports/top-spending-customers")
    public ResponseEntity<?> getTopSpendingCustomers(
            @RequestParam LocalDate from,
            @RequestParam LocalDate to
    ){
        List<TopSpendingResponseDto> topSpending = reportService.getTopSpending(from, to);
        return new ResponseEntity<>(topSpending, HttpStatus.OK);
    }
    @GetMapping("/reports/new-accounts-this-month")
    public ResponseEntity<?> getNewAccountsThisMonth(){
        List<UserResponseDto> newAccounts = reportService.getNewAccountsThisMonth();
        return new ResponseEntity<>(newAccounts, HttpStatus.OK);
    }
    @GetMapping("reports/invoices-over-time")
    public ResponseEntity<?> getInvoicesOverTime(@RequestParam LocalDate from, @RequestParam LocalDate to) {
        long invoiceCount = reportService.countInvoicesOverTime(from, to);
        return new ResponseEntity<>(invoiceCount, HttpStatus.OK);
    }


}
