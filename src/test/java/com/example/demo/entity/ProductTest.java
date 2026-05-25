package com.example.demo.entity;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {
    @Test
    void testProduct_allFieldsComplete() {

        Product product = new Product();

        UUID productId = UUID.randomUUID();
        Category category = new Category();

        product.setProductId(productId);
        product.setName("Laptop");
        product.setDescription("High performance laptop");
        product.setBrand("Dell");
        product.setBasePrice(55000.0);
        product.setStatus("ACTIVE");
        product.setCategory(category);

        assertEquals(productId, product.getProductId());
        assertEquals("Laptop", product.getName());
        assertEquals("High performance laptop", product.getDescription());
        assertEquals("Dell", product.getBrand());
        assertEquals(55000.0, product.getBasePrice());
        assertEquals("ACTIVE", product.getStatus());
        assertNotNull(product.getCategory());
    }

    @Test
    void testProduct_nullValues() {

        Product product = new Product();

        product.setName(null);
        product.setDescription(null);
        product.setBrand(null);
        product.setBasePrice(null);
        product.setStatus(null);
        product.setCategory(null);

        assertNull(product.getName());
        assertNull(product.getDescription());
        assertNull(product.getBrand());
        assertNull(product.getBasePrice());
        assertNull(product.getStatus());
        assertNull(product.getCategory());
    }

    @Test
    void testProduct_multipleUpdates() {

        Product product = new Product();

        product.setName("Phone");
        assertEquals("Phone", product.getName());

        product.setName("Tablet");
        assertEquals("Tablet", product.getName());
    }

    @Test
    void testProduct_priceEdge() {

        Product product = new Product();

        product.setBasePrice(0.0);
        assertEquals(0.0, product.getBasePrice());

        product.setBasePrice(999999.0);
        assertEquals(999999.0, product.getBasePrice());
    }

    @Test
    void testProduct_categoryAssignment() {

        Product product = new Product();

        Category cat1 = new Category();
        Category cat2 = new Category();

        product.setCategory(cat1);
        assertEquals(cat1, product.getCategory());

        product.setCategory(cat2);
        assertEquals(cat2, product.getCategory());
    }

    @Test
    void testProduct_constructor() {

        Product product = new Product();

        assertNotNull(product);
    }

}