package com.ra.service.product;

import com.ra.model.dto.ProductResponseDto;
import com.ra.model.entity.Product;
import com.ra.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<ProductResponseDto> pagination(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        List<ProductResponseDto> productResponseDtos = products.getContent().stream()
                .map(this::toDto).toList();
        return new PageImpl<>(productResponseDtos, pageable, products.getTotalElements());
    }

    @Override
    public List<ProductResponseDto> search(String searchValue) {
        List<Product> products = productRepository.findByProductNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(searchValue, searchValue);
        return products.stream().map(this::toDto).toList();
    }

    @Override
    public List<ProductResponseDto> getFeaturedProducts() {
        List<Product> featuredProduct = productRepository.findByIsFeaturedTrue();
        return featuredProduct.stream().map(this::toDto).toList();
    }

    @Override
    public List<ProductResponseDto> getTop10SaleProducts() {
        List<Product> top10Products = productRepository.findTop10ByOrderDetails();
        return top10Products.stream().map(this::toDto).toList();
    }

    @Override
    public List<ProductResponseDto> getTop10ProductNew() {
        List<Product> top10ProductNews = productRepository.findTop10ByOrderByCreatedAtDesc();
        return top10ProductNews.stream().map(this::toDto).toList();
    }

    private ProductResponseDto toDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .image(product.getImage())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
