package com.example.demo.dto;

import java.util.UUID;

public class OrderView {

    private UUID orderId;

    public OrderView() {
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }
}
