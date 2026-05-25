package com.example.demo.dto;

import java.util.UUID;

public class CartItemView {

    private UUID variantId;

    public CartItemView() {
    }

    public UUID getVariantId() {
        return variantId;
    }

    public void setVariantId(UUID variantId) {
        this.variantId = variantId;
    }
}
