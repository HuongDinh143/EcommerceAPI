package com.ra.service.product;

import com.ra.model.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<ProductResponseDto> pagination(Pageable pageable);
    List<ProductResponseDto> search(String searchValue);
    List<ProductResponseDto> getFeaturedProducts();
    List<ProductResponseDto> getTop10SaleProducts();
    List<ProductResponseDto> getTop10ProductNew();
}
