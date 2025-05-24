package com.ecom.monolith.controller;

import com.ecom.monolith.Dto.ProductDto;
import com.ecom.monolith.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getAllProducts(){
        return productService.getAllProducts();
    }

    @GetMapping("{id}")
    public ProductDto findById(@PathVariable("id") Long id){
        return productService.findById(id);
    }

    @GetMapping("/search")
    public List<ProductDto> findByKeyword(@RequestParam("keyword") String keyword){
        return productService.findByKeyword(keyword);
    }

    @PostMapping
    public ProductDto addProduct(@Valid @RequestBody ProductDto productDto){
        return productService.addProduct(productDto);
    }

    @PutMapping("{id}")
    public ProductDto updateProduct(@PathVariable("id") Long id, @Valid @RequestBody ProductDto productDto){
        return productService.updateProduct(id, productDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id){
        String response = productService.deleteProduct(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
