package com.ra.service.product;

import com.ra.exception.CustomException;
import com.ra.model.dto.response.ProductCartResponseDto;
import com.ra.model.dto.request.ProductRequestDto;
import com.ra.model.dto.response.ProductResponseDto;
import com.ra.model.entity.*;
import com.ra.repository.CategoryRepository;
import com.ra.repository.OrderDetailRepository;
import com.ra.repository.ProductRepository;
import com.ra.repository.ShoppingCartRepository;
import com.ra.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Autowired
    private UploadService uploadService;

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
    public List<ProductResponseDto> getProductByCategory(Long categoryId){
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new CustomException("Category not found"));
        List<Product> products = productRepository.findByCategory(category);
        return products.stream().map(this::toDto).toList();
    }

    @Override
    public ProductResponseDto getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new CustomException("Product not found"));
        return toDto(product);
    }

    @Override
    public List<ProductCartResponseDto> getProductsInCart(Long userId) {
        ShoppingCart cart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException("ShoppingCart not found for user id: " + userId));
        List<ProductCartResponseDto> listProduct = new ArrayList<>();
        for (CartItem item : cart.getItems()) {
            Product product = productRepository.findById(item.getId())
                    .orElseThrow(()-> new CustomException("Product not found for product id: " + item.getProduct().getId()));
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

    @Override
    public ProductResponseDto addNewProduct(ProductRequestDto requestDto) {
        String fileName = uploadService.uploadFile(requestDto.getImage());
        Category category = categoryRepository.findById(requestDto.getCatId())
                .orElseThrow(()->new CustomException("Category not found"));
        Product product = Product.builder()
                .sku(UUID.randomUUID().toString())
                .productName(requestDto.getProductName())
                .description(requestDto.getDescription())
                .unitPrice(requestDto.getUnitPrice())
                .stockQuantity(requestDto.getStockQuantity())
                .image(fileName)
                .category(category)
                .createdAt(LocalDate.now())
                .isFeatured(requestDto.getIsFeatured())
                .build();
        productRepository.save(product);
        return toDto(product);
    }

    @Override
    public ProductResponseDto updateProduct(Long productId, ProductRequestDto requestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new CustomException("Product not found"));
        String fileName = product.getImage();
        Boolean isUpdated = false;
        if (requestDto.getImage() != null) {
            fileName = uploadService.uploadFile(requestDto.getImage());
            product.setImage(fileName);
            isUpdated = true;
        }
        if (requestDto.getProductName() != null) {
            product.setProductName(requestDto.getProductName());
            isUpdated = true;
        }
        if (requestDto.getDescription() != null) {
            product.setDescription(requestDto.getDescription());
            isUpdated = true;
        }
        if (requestDto.getUnitPrice() != null) {
            product.setUnitPrice(requestDto.getUnitPrice());
            isUpdated = true;
        }
        if (requestDto.getStockQuantity() != product.getStockQuantity()) {
            product.setStockQuantity(requestDto.getStockQuantity());
            isUpdated = true;
        }
        if (requestDto.getIsFeatured() != null) {
            product.setIsFeatured(requestDto.getIsFeatured());
            isUpdated = true;
        }
        if (requestDto.getCatId() != null) {
            Category category = categoryRepository.findById(requestDto.getCatId())
                    .orElseThrow(()->new CustomException("Category not found"));
            product.setCategory(category);
            isUpdated = true;
        }
        if (isUpdated) {
            product.setUpdatedAt(LocalDate.now());
            productRepository.save(product);
        }

        return toDto(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        Product productDelete = productRepository.findById(productId)
                .orElseThrow(()->new CustomException("Product not found"));
        productRepository.delete(productDelete);
    }


    private ProductResponseDto toDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .productName(product.getProductName())
                .image(product.getImage())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .catName(product.getCategory().getCatName())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
