package com.example.demo.dto;

import java.util.List;
import java.util.UUID;

public class CartView {

    private UUID userId;
    private List<CartItemView> items;

    public CartView() {
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public List<CartItemView> getItems() {
        return items;
    }

    public void setItems(List<CartItemView> items) {
        this.items = items;
    }
}