package com.example.demo.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {

    @Test
    void testOrder_allFieldsComplete() {

        Order order = new Order();

        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("1500.50");
        LocalDateTime now = LocalDateTime.now();

        order.setOrderId(orderId);
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setWarehouseId(warehouseId);
        order.setTotalAmount(amount);
        order.setStatus("CREATED");
        order.setPaymentStatus("PENDING");
        order.setCreatedAt(now);

        assertEquals(orderId, order.getOrderId());
        assertEquals(userId, order.getUserId());
        assertEquals(addressId, order.getAddressId());
        assertEquals(warehouseId, order.getWarehouseId());
        assertEquals(amount, order.getTotalAmount());
        assertEquals("CREATED", order.getStatus());
        assertEquals("PENDING", order.getPaymentStatus());
        assertEquals(now, order.getCreatedAt());
    }

    @Test
    void testOrder_nullValues() {

        Order order = new Order();

        order.setUserId(null);
        order.setAddressId(null);
        order.setWarehouseId(null);
        order.setTotalAmount(null);
        order.setStatus(null);
        order.setPaymentStatus(null);

        assertNull(order.getUserId());
        assertNull(order.getAddressId());
        assertNull(order.getWarehouseId());
        assertNull(order.getTotalAmount());
        assertNull(order.getStatus());
        assertNull(order.getPaymentStatus());
    }

    @Test
    void testOrder_multipleUpdates() {

        Order order = new Order();

        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();

        order.setUserId(first);
        assertEquals(first, order.getUserId());

        order.setUserId(second);
        assertEquals(second, order.getUserId());
    }

    @Test
    void testOrder_statusUpdate() {

        Order order = new Order();

        order.setStatus("CREATED");
        assertEquals("CREATED", order.getStatus());

        order.setStatus("SHIPPED");
        assertEquals("SHIPPED", order.getStatus());
    }

    @Test
    void testOrder_amountEdge() {

        Order order = new Order();

        order.setTotalAmount(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, order.getTotalAmount());

        order.setTotalAmount(new BigDecimal("999999"));
        assertEquals(new BigDecimal("999999"), order.getTotalAmount());
    }

    @Test
    void testOrder_constructor() {

        Order order = new Order();

        assertNotNull(order);
    }

}
