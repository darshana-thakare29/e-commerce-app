package com.example.demo.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void testInventory_allFieldsComplete() {

        Inventory inventory = new Inventory();

        UUID inventoryId = UUID.randomUUID();
        UUID variantId = UUID.randomUUID();
        UUID warehouseId = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        inventory.setInventoryId(inventoryId);
        inventory.setVariantId(variantId);
        inventory.setWarehouseId(warehouseId);
        inventory.setStockQuantity(50);
        inventory.setReservedQuantity(20);
        inventory.setCreatedAt(createdAt);
        inventory.setUpdatedAt(updatedAt);

        assertEquals(inventoryId, inventory.getInventoryId());
        assertEquals(variantId, inventory.getVariantId());
        assertEquals(warehouseId, inventory.getWarehouseId());
        assertEquals(50, inventory.getStockQuantity());
        assertEquals(20, inventory.getReservedQuantity());
        assertEquals(createdAt, inventory.getCreatedAt());
        assertEquals(updatedAt, inventory.getUpdatedAt());
    }

    @Test
    void testInventory_nullValues() {

        Inventory inventory = new Inventory();

        inventory.setVariantId(null);
        inventory.setWarehouseId(null);
        inventory.setStockQuantity(null);
        inventory.setReservedQuantity(null);

        assertNull(inventory.getVariantId());
        assertNull(inventory.getWarehouseId());
        assertNull(inventory.getStockQuantity());
        assertNull(inventory.getReservedQuantity());
    }

    @Test
    void testInventory_multipleUpdates() {

        Inventory inventory = new Inventory();

        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();

        inventory.setVariantId(first);
        assertEquals(first, inventory.getVariantId());

        inventory.setVariantId(second);
        assertEquals(second, inventory.getVariantId());
    }

    @Test
    void testInventory_quantityEdge() {

        Inventory inventory = new Inventory();

        inventory.setStockQuantity(0);
        assertEquals(0, inventory.getStockQuantity());

        inventory.setStockQuantity(999);
        assertEquals(999, inventory.getStockQuantity());
    }

    @Test
    void testInventory_constructor() {

        Inventory inventory = new Inventory();

        assertNotNull(inventory);
    }
}