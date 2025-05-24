package com.ecom.monolith.service;

import com.ecom.monolith.Dto.ProductDto;
import com.ecom.monolith.Mapper.ProductMapper;
import com.ecom.monolith.exception.ResourceNotFound;
import com.ecom.monolith.model.Product;
import com.ecom.monolith.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        Product product = productMapper.toEntity(productDto);
        Product savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        return productRepository.findById(id).map(product -> {
            product.setName(productDto.getName());
            product.setDescription(productDto.getDescription());
            product.setPrice(productDto.getPrice());
            product.setStockQuantity(productDto.getStockQuantity());
            product.setCategory(productDto.getCategory());
            product.setImageUrl(productDto.getImageUrl());
            Product updatedProduct = productRepository.save(product);
            return productMapper.toDto(updatedProduct);
        }).orElseThrow(() -> new ResourceNotFound("Resource not found with id: " + id));
    }

    @Override
    public String deleteProduct(Long id) {
            productRepository.findById(id).map(product -> {
            product.setActive(false);
            return productRepository.save(product);
        }).orElseThrow(() -> new ResourceNotFound("Resource not found with id: " + id));
        return "Product deleted successfully";
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findByActiveTrue().stream().map(
                productMapper::toDto).toList();
    }

    @Override
    public ProductDto findById(Long id) {
        return productRepository.findById(id).map(productMapper::toDto)
                .orElseThrow(() -> new ResourceNotFound("Resource not found with id: " + id));
    }

    @Override
    public  List<ProductDto> findByKeyword(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(keyword).stream()
                .map(productMapper::toDto).toList();
    }
}
