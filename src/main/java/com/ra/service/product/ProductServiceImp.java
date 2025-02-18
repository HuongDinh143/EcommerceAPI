package com.ra.service.product;

import com.ra.model.dto.ProductCartResponseDto;
import com.ra.model.dto.ProductResponseDto;
import com.ra.model.entity.*;
import com.ra.repository.CategoryRepository;
import com.ra.repository.OrderDetailRepository;
import com.ra.repository.ProductRepository;
import com.ra.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

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
        List<OrderDetail> orderDetails = orderDetailRepository.findTop10ByOrderByOrderQuantityDesc();
        List<Product> products = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            products.add(orderDetail.getProduct());
        }
        return products.stream().map(this::toDto).toList();

    }

    @Override
    public List<ProductResponseDto> getNewProduct() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());
        List<Product> newProducts = productRepository.findByCreatedAtBetween(startOfMonth, endOfMonth);
        return newProducts.stream().map(this::toDto).toList();
    }

    @Override
    public List<ProductResponseDto> getProductByCategory(Long categoryId) throws Exception {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new Exception("Category not found"));
        List<Product> products = productRepository.findByCategory(category);
        return products.stream().map(this::toDto).toList();
    }

    @Override
    public ProductResponseDto getProductById(Long productId) throws Exception {
        Product product = productRepository.findById(productId).orElseThrow(()-> new Exception("Product not found"));
        return toDto(product);
    }

    @Override
    public List<ProductCartResponseDto> getProductsInCart(Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("ShoppingCart not found for user id: " + userId));
        List<ProductCartResponseDto> listProduct = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(()-> new RuntimeException("Product not found for product id: " + item.getProductId()));
            ProductCartResponseDto dto = new ProductCartResponseDto();
            dto.setProductId(product.getId());
            dto.setName(product.getProductName());
            dto.setPrice(product.getUnitPrice());
            dto.setQuantity(item.getQuantity());
            dto.setTotalPrice(product.getUnitPrice()*item.getQuantity());
            listProduct.add(dto);
        }
        return listProduct;


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
