package com.example.demo.controller;

import com.example.demo.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<UUID> checkout(@RequestParam UUID userId,
                                         @RequestParam UUID addressId,
                                         @RequestParam UUID warehouseId) {
        return ResponseEntity.ok(orderService.checkout(userId, addressId, warehouseId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Object> getOrder(@PathVariable UUID orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    @GetMapping("/user")
    public ResponseEntity<List<Object>> getUserOrders(@RequestParam UUID userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<String> cancelOrder(@PathVariable UUID orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok("Order cancelled successfully");
    }
}