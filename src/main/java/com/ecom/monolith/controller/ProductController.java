package com.ecom.monolith.controller;

import com.ecom.monolith.Dto.ProductDto;
import com.ecom.monolith.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getAllProducts() {
        logger.info("GET /api/products - Fetching all products");
        return productService.getAllProducts();
    }

    @GetMapping("{id}")
    public ProductDto findById(@PathVariable("id") Long id) {
        logger.info("GET /api/products/{} - Fetching product by ID", id);
        return productService.findById(id);
    }

    @GetMapping("/search")
    public List<ProductDto> findByKeyword(@RequestParam("keyword") String keyword) {
        logger.info("GET /api/products/search?keyword={} - Searching products", keyword);
        return productService.findByKeyword(keyword);
    }

    @PostMapping
    public ProductDto addProduct(@Valid @RequestBody ProductDto productDto) {
        logger.info("POST /api/products - Adding new product with name={}", productDto.getName());
        return productService.addProduct(productDto);
    }

    @PutMapping("{id}")
    public ProductDto updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductDto productDto) {
        logger.info("PUT /api/products/{} - Updating product with name={}", id, productDto.getName());
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id) {
        logger.info("DELETE /api/products/{} - Deleting product", id);
        String response = productService.deleteProduct(id);
        logger.info("Product deleted. Response: {}", response);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
