package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
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
    private final PaymentRepository paymentRepository;
    private final InventoryRepository inventoryRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            CartRepository cartRepository,
                            CartItemRepository cartItemRepository,
                            PaymentRepository paymentRepository,
                            InventoryRepository inventoryRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.paymentRepository = paymentRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public UUID checkout(UUID userId, UUID addressId, UUID warehouseId) {

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

            Inventory inventory = inventoryRepository.findByVariantId(item.getVariantId())
                    .orElseThrow(() -> new RuntimeException("Inventory not found"));

            if (inventory.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Out of stock");
            }

            inventory.setReservedQuantity(
                    inventory.getReservedQuantity() + item.getQuantity()
            );

            inventoryRepository.save(inventory);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(UUID.randomUUID());
            orderItem.setOrderId(order.getOrderId());
            orderItem.setVariantId(item.getVariantId());
            orderItem.setQuantity(item.getQuantity());

            BigDecimal price = BigDecimal.valueOf(100); // placeholder price
            orderItem.setPrice(price);

            total = total.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));

            orderItemRepository.save(orderItem);
        }

        order.setTotalAmount(total);
        orderRepository.save(order);

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(order.getOrderId());
        payment.setUserId(userId);
        payment.setAmount(total);
        payment.setStatus("PENDING");
        paymentRepository.save(payment);

        cartItemRepository.deleteAll(cartItems);

        return order.getOrderId();
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

