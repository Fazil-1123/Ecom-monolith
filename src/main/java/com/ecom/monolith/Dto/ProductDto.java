package com.ecom.monolith.Dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductDto {

    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @Size(max = 1000, message = "Description can be up to 1000 characters")
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Stock quantity is required")
    private Integer stockQuantity;

    @NotBlank(message = "Category is required")
    private String category;

    private String imageUrl;
}
