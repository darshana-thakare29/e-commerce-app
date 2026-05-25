package com.example.demo.dto;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartItemViewTest {

    @Test
    void testCartItemView_full() {

        CartItemView item = new CartItemView();

        UUID variantId = UUID.randomUUID();

        item.setVariantId(variantId);

        assertEquals(variantId, item.getVariantId());
    }
}
