package com.ecom.monolith.Mapper;

import com.ecom.monolith.Dto.CartResponse;
import com.ecom.monolith.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartItem toEntity(CartResponse cartResponse);

    @Mapping(source = "product.id", target = "productId")
    CartResponse toDto(CartItem cartItem);
}
