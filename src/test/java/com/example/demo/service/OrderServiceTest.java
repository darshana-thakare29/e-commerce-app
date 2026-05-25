package com.example.demo.service;

import com.example.demo.entity.*;
import com.example.demo.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private OrderItemRepository orderItemRepository;
    @Mock private CartRepository cartRepository;
    @Mock private CartItemRepository cartItemRepository;
    @Mock private PaymentRepository paymentRepository;
    @Mock private InventoryRepository inventoryRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void testCheckout_success() {

        UUID userId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setVariantId(UUID.randomUUID());
        item.setQuantity(2);

        Inventory inventory = new Inventory();
        inventory.setStockQuantity(10);
        inventory.setReservedQuantity(0);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId())).thenReturn(List.of(item));
        when(inventoryRepository.findByVariantId(item.getVariantId()))
                .thenReturn(Optional.of(inventory));

        var response = orderService.checkout(userId, addressId, warehouseId);

        assertTrue(response.isSuccess());
        verify(orderRepository).save(any(Order.class));
        verify(paymentRepository).save(any(Payment.class));
        verify(cartItemRepository).deleteAll(List.of(item));
    }

    @Test
    void testCheckout_cartEmpty() {

        UUID userId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId())).thenReturn(List.of());

        assertThrows(RuntimeException.class,
                () -> orderService.checkout(userId, UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void testCancelOrder() {

        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.cancelOrder(orderId);

        assertEquals("CANCELLED", order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void testCheckout_cartNotFound() {

        UUID userId = UUID.randomUUID();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> orderService.checkout(userId, UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void testCheckout_inventoryNotFound() {

        UUID userId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setVariantId(UUID.randomUUID());
        item.setQuantity(2);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId())).thenReturn(List.of(item));
        when(inventoryRepository.findByVariantId(item.getVariantId()))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> orderService.checkout(userId, UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void testCheckout_outOfStock() {

        UUID userId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setVariantId(UUID.randomUUID());
        item.setQuantity(5);

        Inventory inventory = new Inventory();
        inventory.setStockQuantity(2); // less stock

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId())).thenReturn(List.of(item));
        when(inventoryRepository.findByVariantId(item.getVariantId()))
                .thenReturn(Optional.of(inventory));

        assertThrows(RuntimeException.class,
                () -> orderService.checkout(userId, UUID.randomUUID(), UUID.randomUUID()));
    }

    @Test
    void testGetOrder_success() {

        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Object result = orderService.getOrder(orderId);

        assertNotNull(result);
    }

    @Test
    void testGetOrder_notFound() {

        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> orderService.getOrder(orderId));
    }

    @Test
    void testGetUserOrders() {

        UUID userId = UUID.randomUUID();

        when(orderRepository.findByUserId(userId)).thenReturn(List.of(new Order()));

        var result = orderService.getUserOrders(userId);

        assertEquals(1, result.size());
    }

    @Test
    void testCancelOrder_notFound() {

        UUID orderId = UUID.randomUUID();

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> orderService.cancelOrder(orderId));
    }


}