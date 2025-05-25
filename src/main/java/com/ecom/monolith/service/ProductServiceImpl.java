package com.ecom.monolith.service;

import com.ecom.monolith.Dto.ProductDto;
import com.ecom.monolith.Mapper.ProductMapper;
import com.ecom.monolith.exception.ResourceNotFound;
import com.ecom.monolith.model.Product;
import com.ecom.monolith.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        logger.info("Adding new product: {}", productDto.getName());
        Product product = productMapper.toEntity(productDto);
        Product savedProduct = productRepository.save(product);
        logger.info("Product added with ID: {}", savedProduct.getId());
        return productMapper.toDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        logger.info("Updating product ID: {}", id);
        return productRepository.findById(id).map(product -> {
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setStockQuantity(productDto.getStockQuantity());
            product.setCategory(productDto.getCategory());
            product.setImageUrl(productDto.getImageUrl());
            Product updatedProduct = productRepository.save(product);
            logger.info("Product updated: ID={}", updatedProduct.getId());
            return productMapper.toDto(updatedProduct);
        }).orElseThrow(() -> {
            logger.warn("Product not found for update: ID={}", id);
            return new ResourceNotFound("Resource not found with id: " + id);
        });
    }

    @Override
    public String deleteProduct(Long id) {
        logger.info("Deleting (deactivating) product ID: {}", id);
        productRepository.findById(id).map(product -> {
            product.setActive(false);
            Product saved = productRepository.save(product);
            logger.info("Product deactivated: ID={}", saved.getId());
            return saved;
        }).orElseThrow(() -> {
            logger.warn("Product not found for deletion: ID={}", id);
            return new ResourceNotFound("Resource not found with id: " + id);
        });
        return "Product deleted successfully";
    }

    @Override
    public List<ProductDto> getAllProducts() {
        logger.info("Fetching all active products");
        return productRepository.findByActiveTrue().stream()
                .map(productMapper::toDto).toList();
    }

    @Override
    public ProductDto findById(Long id) {
        logger.info("Fetching product by ID: {}", id);
        return productRepository.findById(id).map(productMapper::toDto)
                .orElseThrow(() -> {
                    logger.warn("Product not found: ID={}", id);
                    return new ResourceNotFound("Resource not found with id: " + id);
                });
    }

    @Override
    public List<ProductDto> findByKeyword(String keyword) {
        logger.info("Searching products by keyword: {}", keyword);
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(keyword).stream()
                .map(productMapper::toDto).toList();
    }
}
