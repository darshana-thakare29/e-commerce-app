package com.example.demo.dto;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartViewTest {

    @Test
    void testCartView_full() {

        CartView view = new CartView();

        UUID userId = UUID.randomUUID();

        view.setUserId(userId);
        view.setItems(List.of());

        assertEquals(userId, view.getUserId());
        assertNotNull(view.getItems());
    }
}