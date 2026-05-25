package com.example.demo.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {


    @Test
    void testCart_full() {

        Cart cart = new Cart();

        UUID cartId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        cart.setCartId(cartId);
        cart.setUserId(userId);

        assertEquals(cartId, cart.getCartId());
        assertEquals(userId, cart.getUserId());
    }

    @Test
    void testAllFields() {

        Cart cart = new Cart();

        UUID cartId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        cart.setCartId(cartId);
        cart.setUserId(userId);
        cart.setCreatedAt(createdAt);
        cart.setUpdatedAt(updatedAt);

        assertEquals(cartId, cart.getCartId());
        assertEquals(userId, cart.getUserId());
        assertEquals(createdAt, cart.getCreatedAt());
        assertEquals(updatedAt, cart.getUpdatedAt());
    }

    @Test
    void testConstructor() {

        Cart cart = new Cart();

        assertNotNull(cart);
    }

    @Test
    void testToString() {

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        assertNotNull(cart.toString());
    }
}
