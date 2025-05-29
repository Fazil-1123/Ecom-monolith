package com.ecom.monolith.service;

import com.ecom.monolith.Dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductDto addProduct(ProductDto productDto);

    ProductDto updateProduct(Long id, ProductDto productDto);

    String deleteProduct(Long id);

    List<ProductDto> getAllProducts();

    ProductDto findById(Long id);

    List<ProductDto> findByKeyword(String keyword);

    String uploadImageToS3(MultipartFile file);
}
