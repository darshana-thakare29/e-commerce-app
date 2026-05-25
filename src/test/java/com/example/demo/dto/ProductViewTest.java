package com.example.demo.dto;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductViewTest {

    @Test
    void testProductView_full() {

        ProductView view = new ProductView();

        UUID id = UUID.randomUUID();

        view.setProductId(id);

        assertEquals(id, view.getProductId());
    }
}
