package com.ecom.monolith.service;

import com.ecom.monolith.Dto.OrderResponse;
import com.ecom.monolith.Mapper.OrderMapper;
import com.ecom.monolith.exception.ResourceNotFound;
import com.ecom.monolith.model.*;
import com.ecom.monolith.repositories.CartItemRepository;
import com.ecom.monolith.repositories.OrderRepository;
import com.ecom.monolith.repositories.UsersRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final CartItemRepository cartItemRepository;
    private final UsersRepository usersRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderServiceImpl(CartItemRepository cartItemRepository, UsersRepository usersRepository,
                            OrderRepository orderRepository, OrderMapper orderMapper) {
        this.cartItemRepository = cartItemRepository;
        this.usersRepository = usersRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public OrderResponse placeOrder(String userId) {
        logger.info("Placing order for userId={}", userId);

        BigDecimal totalPrice = BigDecimal.ZERO;

        Users users = usersRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ResourceNotFound("User does not exist with id: " + userId));

        List<CartItem> cartItems = cartItemRepository.findByUsersId(Long.valueOf(userId)).stream().toList();

        if (cartItems.isEmpty()) {
            logger.warn("User with userId={} tried to place order with empty cart", userId);
            throw new ResourceNotFound("User doesn't have any items in cart");
        }

        List<OrderItem> orderItemList = new ArrayList<>();
        Order order = new Order();
        order.setUsers(users);
        order.setStatus(OrderStatus.ORDERED);

        for (CartItem cartItem : cartItems) {
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

        logger.info("Order placed successfully for userId={}, orderId={}, total={}",
                userId, placedOrder.getId(), placedOrder.getTotalAmount());

        return orderMapper.toDto(placedOrder);
    }
}
