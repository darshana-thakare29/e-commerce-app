package com.example.demo.dto;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderViewTest {

    @Test
    void testOrderView_full() {

        OrderView view = new OrderView();

        UUID orderId = UUID.randomUUID();

        view.setOrderId(orderId);

        assertEquals(orderId, view.getOrderId());
    }
}