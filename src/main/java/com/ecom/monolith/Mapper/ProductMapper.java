package com.ecom.monolith.Mapper;

import com.ecom.monolith.Dto.ProductDto;
import com.ecom.monolith.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    Product toEntity(ProductDto productDto);

    ProductDto toDto(Product product);
}
