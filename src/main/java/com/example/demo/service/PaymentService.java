package com.example.demo.service;

import com.example.demo.entity.Payment;
import java.util.UUID;

public interface PaymentService {
    Payment processPayment(UUID orderId, UUID userId, String method, String cardLastFour);
}
