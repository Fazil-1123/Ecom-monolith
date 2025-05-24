package com.ecom.monolith.service;

import com.ecom.monolith.Dto.ProductDto;

import java.util.List;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(Long id, ProductDto productDto);

    String deleteProduct(Long id);

    List<ProductDto> getAllProducts();

    ProductDto findById(Long id);

    List<ProductDto> findByKeyword(String keyword);
}
