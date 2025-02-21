package com.ra.service.product;

import com.ra.model.dto.response.ProductCartResponseDto;
import com.ra.model.dto.request.ProductRequestDto;
import com.ra.model.dto.response.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductResponseDto> pagination(Pageable pageable);
    List<ProductResponseDto> search(String searchValue);
    List<ProductResponseDto> getFeaturedProducts();
    List<ProductResponseDto> getTop10SaleProducts();
    List<ProductResponseDto> getNewProduct();
    List<ProductResponseDto> getProductByCategory(Long categoryId) throws Exception;
    ProductResponseDto getProductById(Long productId) throws Exception;
    List<ProductCartResponseDto> getProductsInCart(Long userId);
    ProductResponseDto addNewProduct(ProductRequestDto requestDto);
    ProductResponseDto updateProduct(Long productId, ProductRequestDto requestDto);
    void deleteProduct(Long productId);
}
