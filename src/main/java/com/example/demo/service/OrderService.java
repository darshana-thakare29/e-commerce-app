package com.example.demo.service;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    UUID checkout(UUID userId, UUID addressId, UUID warehouseId);

    Object getOrder(UUID orderId);

    List<Object> getUserOrders(UUID userId);

    void cancelOrder(UUID orderId);
}
