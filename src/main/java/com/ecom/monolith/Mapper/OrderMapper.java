package com.ecom.monolith.Mapper;

import com.ecom.monolith.Dto.OrderItemResponse;
import com.ecom.monolith.Dto.OrderResponse;
import com.ecom.monolith.model.Order;
import com.ecom.monolith.model.OrderItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponse toDto(Order order);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "price", source = "price")
    OrderItemResponse toDto(OrderItem item);
}
