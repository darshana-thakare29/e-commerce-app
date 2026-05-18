package com.example.demo.service;

import com.example.demo.dto.ApiResponse;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    ApiResponse<UUID> checkout(UUID userId, UUID addressId, UUID warehouseId);

    Object getOrder(UUID orderId);

    List<Object> getUserOrders(UUID userId);

    void cancelOrder(UUID orderId);
}
