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

    @Test
    void testCheckout_reservedQuantityAlreadyExists() {

        UUID userId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setVariantId(UUID.randomUUID());
        item.setQuantity(2);

        Inventory inventory = new Inventory();
        inventory.setStockQuantity(10);
        inventory.setReservedQuantity(5); // 🔥 NEW BRANCH

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId()))
                .thenReturn(List.of(item));
        when(inventoryRepository.findByVariantId(item.getVariantId()))
                .thenReturn(Optional.of(inventory));

        var response = orderService.checkout(userId, UUID.randomUUID(), UUID.randomUUID());

        assertTrue(response.isSuccess());
    }

    @Test
    void testCheckout_multipleItems() {

        UUID userId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item1 = new CartItem();
        item1.setVariantId(UUID.randomUUID());
        item1.setQuantity(2);

        CartItem item2 = new CartItem();
        item2.setVariantId(UUID.randomUUID());
        item2.setQuantity(3);

        Inventory inventory = new Inventory();
        inventory.setStockQuantity(10);
        inventory.setReservedQuantity(0); // ✅ FIX

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId()))
                .thenReturn(List.of(item1, item2));
        when(inventoryRepository.findByVariantId(any()))
                .thenReturn(Optional.of(inventory));

        var response = orderService.checkout(userId, UUID.randomUUID(), UUID.randomUUID());

        assertTrue(response.isSuccess());
    }
    @Test
    void testCheckout_exactStockMatch() {

        UUID userId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setVariantId(UUID.randomUUID());
        item.setQuantity(5);

        Inventory inventory = new Inventory();
        inventory.setStockQuantity(5);
        inventory.setReservedQuantity(0); // exact match

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId()))
                .thenReturn(List.of(item));
        when(inventoryRepository.findByVariantId(item.getVariantId()))
                .thenReturn(Optional.of(inventory));

        var response = orderService.checkout(userId, UUID.randomUUID(), UUID.randomUUID());

        assertTrue(response.isSuccess());
    }
    @Test
    void testCheckout_differentInventoryPerItem() {

        UUID userId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item1 = new CartItem();
        item1.setVariantId(UUID.randomUUID());
        item1.setQuantity(2);

        CartItem item2 = new CartItem();
        item2.setVariantId(UUID.randomUUID());
        item2.setQuantity(1);

        Inventory inv1 = new Inventory();
        inv1.setStockQuantity(10);
        inv1.setReservedQuantity(0);

        Inventory inv2 = new Inventory();
        inv2.setStockQuantity(5);
        inv2.setReservedQuantity(0);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId()))
                .thenReturn(List.of(item1, item2));

        when(inventoryRepository.findByVariantId(item1.getVariantId()))
                .thenReturn(Optional.of(inv1));
        when(inventoryRepository.findByVariantId(item2.getVariantId()))
                .thenReturn(Optional.of(inv2));

        var response = orderService.checkout(userId, UUID.randomUUID(), UUID.randomUUID());

        assertTrue(response.isSuccess());
    }
    @Test
    void testCancelOrder_alreadyCancelled() {

        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setOrderId(orderId);
        order.setStatus("CANCELLED");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.cancelOrder(orderId);

        verify(orderRepository).save(order);
    }
    @Test
    void testGetUserOrders_empty() {

        UUID userId = UUID.randomUUID();

        when(orderRepository.findByUserId(userId)).thenReturn(List.of());

        var result = orderService.getUserOrders(userId);

        assertTrue(result.isEmpty());
    }
    @Test
    void testGetUserOrders_nullReturn() {

        UUID userId = UUID.randomUUID();

        when(orderRepository.findByUserId(userId)).thenReturn(null);

        var result = orderService.getUserOrders(userId);

        assertTrue(result == null || result.isEmpty());
    }
    @Test
    void testCancelOrder_multipleCalls() {

        UUID orderId = UUID.randomUUID();

        Order order = new Order();
        order.setOrderId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.cancelOrder(orderId);
        orderService.cancelOrder(orderId);

        verify(orderRepository, atLeast(2)).save(order);
    }
}