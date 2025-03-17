package com.ra.controller;

import com.ra.model.dto.response.ApiResponse;
import com.ra.model.dto.response.ProductResponseDto;
import com.ra.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping
    public ApiResponse<Page<ProductResponseDto>> getAllProducts(
            @RequestParam(name = "page",defaultValue = "0") int page,
            @RequestParam(name = "limit",defaultValue = "10") int limit,
            @RequestParam(name = "sortBy",defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "orderBy",defaultValue = "asc") String orderBy

    ) {
        Sort sort = orderBy.equalsIgnoreCase("asc") ? Sort.by(sortBy)
                .ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, limit, sort);
        Page<ProductResponseDto> responseDtos = productService.pagination(pageable);
        return new ApiResponse<>(200,"Danh sach Sp",responseDtos);
    }

    @GetMapping("/search/{searchValue}")
    public ApiResponse<List<ProductResponseDto>> searchProducts(
            @PathVariable String searchValue) {
        List<ProductResponseDto> listResult = productService.search(searchValue);
        return new ApiResponse<>(200,"Ket qua tim kiem",listResult);
    }

    @GetMapping("/featured-products")
    public ApiResponse<List<ProductResponseDto>> getFeaturedProducts() {
        List<ProductResponseDto> featuredProducts = productService.getFeaturedProducts();
        return new ApiResponse<>(200,"Danh sach sp ua chuong",featuredProducts);
    }

    @GetMapping("/best-seller-products")
    public ApiResponse<List<ProductResponseDto>> getBestSale() {
        List<ProductResponseDto> bestSaleProducts = productService.getTop10SaleProducts();
        return new ApiResponse<>(200,"Danh sach sp ban chay",bestSaleProducts);
    }

    @GetMapping("/new-products")
    public ApiResponse<List<ProductResponseDto>> getNewProducts() {
        List<ProductResponseDto> newProducts = productService.getNewProduct();
        return new ApiResponse<>(200,"Danh sach sp moi",newProducts);
    }

    @GetMapping("/categories/{categoryId}")
    public ApiResponse<List<ProductResponseDto>> getProductsByCategoryId(@PathVariable Long categoryId
    ) throws Exception {
        List<ProductResponseDto> products = productService.getProductByCategory(categoryId);
        return new ApiResponse<>(200,"Danh sach sp theo danh muc",products);
    }

    @GetMapping("/{productId}")
    public ApiResponse<ProductResponseDto> getProductById(@PathVariable Long productId) throws Exception {
        ProductResponseDto product = productService.getProductById(productId);
        return new ApiResponse<>(200,"Lay sp theo productId",product);
    }



}
