package com.ra.controller;

import com.ra.model.dto.ProductResponseDto;
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
    @GetMapping("/search/{searchValue}")
    public ResponseEntity<List<ProductResponseDto>> searchProducts(
            @PathVariable String searchValue) {
        List<ProductResponseDto> listResult = productService.search(searchValue);
        return new ResponseEntity<>(listResult, HttpStatus.OK);
    }
    @GetMapping("/featured-products")
    public ResponseEntity<List<ProductResponseDto>> getFeaturedProducts() {
        List<ProductResponseDto> featuredProducts = productService.getFeaturedProducts();
        return new ResponseEntity<>(featuredProducts, HttpStatus.OK);
    }
    @GetMapping("/bestSale")
    public ResponseEntity<List<ProductResponseDto>> getBestSale() {
        List<ProductResponseDto> bestSaleProducts = productService.getTop10SaleProducts();
        return new ResponseEntity<>(bestSaleProducts, HttpStatus.OK);
    }


}
