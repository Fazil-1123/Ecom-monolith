package com.ecom.monolith.Mapper;

import com.ecom.monolith.Dto.OrderItemResponse;
import com.ecom.monolith.model.OrderItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderItemResponseMapper {

    OrderItemResponse toDto(OrderItem orderItem);
}
