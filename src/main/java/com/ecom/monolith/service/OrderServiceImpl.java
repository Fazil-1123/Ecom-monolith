package com.ecom.monolith.service;

import com.ecom.monolith.Dto.OrderResponse;
import com.ecom.monolith.Mapper.OrderMapper;
import com.ecom.monolith.exception.ResourceNotFound;
import com.ecom.monolith.model.*;
import com.ecom.monolith.repositories.CartItemRepository;
import com.ecom.monolith.repositories.OrderRepository;
import com.ecom.monolith.repositories.UsersRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    private final CartItemRepository cartItemRepository;

    private final UsersRepository usersRepository;

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderServiceImpl(CartItemRepository cartItemRepository, UsersRepository usersRepository, OrderRepository orderRepository, OrderMapper orderMapper) {
        this.cartItemRepository = cartItemRepository;
        this.usersRepository = usersRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderResponse placeOrder(String userId) {
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        Users users = usersRepository.findById(Long.valueOf(userId)).orElseThrow(
                ()->new ResourceNotFound("User does not exist with id: "+userId)
        );
        List<CartItem> cartItems = cartItemRepository.findByUsersId(Long.valueOf(userId)).stream().toList();
        if(cartItems.isEmpty()){
            throw new ResourceNotFound("User doesnt have any items in cart");
        }
        List<OrderItem> orderItemList = new ArrayList<>();
        Order order = new Order();
        order.setUsers(users);
        order.setStatus(OrderStatus.ORDERED);
        for(CartItem cartItem : cartItems){
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItem.setOrder(order);
            orderItemList.add(orderItem);
            totalPrice = totalPrice.add(cartItem.getPrice() != null ? cartItem.getPrice() : BigDecimal.ZERO);


        }
        order.setItems(orderItemList);
        order.setTotalAmount(totalPrice);
        Order placedOrder = orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);
        return orderMapper.toDto(placedOrder);
    }
}
