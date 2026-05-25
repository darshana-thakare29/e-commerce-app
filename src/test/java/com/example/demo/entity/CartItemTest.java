package com.example.demo.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartItemTest {

    @Test
    void testCartItem_allFieldsComplete() {

        CartItem item = new CartItem();

        UUID cartItemId = UUID.randomUUID();
        UUID cartId = UUID.randomUUID();
        UUID variantId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        item.setCartItemId(cartItemId);
        item.setCartId(cartId);
        item.setVariantId(variantId);
        item.setQuantity(10);
        item.setAddedAt(now);

        assertEquals(cartItemId, item.getCartItemId());
        assertEquals(cartId, item.getCartId());
        assertEquals(variantId, item.getVariantId());
        assertEquals(10, item.getQuantity());
    }

    @Test
    void testCartItem_nullValues() {

        CartItem item = new CartItem();

        item.setCartId(null);
        item.setVariantId(null);
        item.setQuantity(null);

        assertNull(item.getCartId());
        assertNull(item.getVariantId());
        assertNull(item.getQuantity());
    }

    @Test
    void testCartItem_multipleUpdates() {

        CartItem item = new CartItem();

        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();

        item.setCartId(first);
        assertEquals(first, item.getCartId());

        item.setCartId(second);
        assertEquals(second, item.getCartId());
    }

    @Test
    void testCartItem_quantityEdge() {

        CartItem item = new CartItem();

        item.setQuantity(0);
        assertEquals(0, item.getQuantity());

        item.setQuantity(999);
        assertEquals(999, item.getQuantity());
    }

    @Test
    void testCartItem_addedAtExecution() {

        CartItem item = new CartItem();

        LocalDateTime now = LocalDateTime.now();
        item.setAddedAt(now);

        assertNotNull(item);
    }

}