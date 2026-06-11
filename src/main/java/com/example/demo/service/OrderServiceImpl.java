package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.*;
import com.example.demo.kafka.event.OrderEvent;
import com.example.demo.kafka.producer.OrderProducer;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;


    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            CartRepository cartRepository,
                            CartItemRepository cartItemRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Autowired
    private OrderProducer orderProducer;
    @Override
    public ApiResponse<UUID> checkout(UUID userId, UUID addressId, UUID warehouseId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setWarehouseId(warehouseId);
        order.setStatus("PENDING");
        order.setPaymentStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(UUID.randomUUID());
            orderItem.setOrderId(order.getOrderId());
            orderItem.setVariantId(item.getVariantId());
            orderItem.setQuantity(item.getQuantity());

            BigDecimal price = BigDecimal.valueOf(100);
            orderItem.setPrice(price);

            total = total.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));

            orderItemRepository.save(orderItem);
        }

        order.setTotalAmount(total);
        orderRepository.save(order);


        OrderEvent event = new OrderEvent(
                order.getOrderId().toString(),
                userId.toString(),
                total.doubleValue(),
                "CREATED"
        );

        orderProducer.sendOrder(event);

        cartItemRepository.deleteAll(cartItems);

        return new ApiResponse<>(
                true,
                "Order placed successfully",
                order.getOrderId()
        );
    }

    @Override
    public Object getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Object> getUserOrders(UUID userId) {
        return (List) orderRepository.findByUserId(userId);
    }

    @Override
    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }
}

