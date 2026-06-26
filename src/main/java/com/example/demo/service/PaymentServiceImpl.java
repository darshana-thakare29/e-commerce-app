package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.Payment;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Payment processPayment(UUID orderId, UUID userId, String method, String cardLastFour) {
        String normalizedMethod = method == null ? "COD" : method.trim().toUpperCase(Locale.ROOT);
        if (!normalizedMethod.equals("COD") && !normalizedMethod.equals("DUMMY_CARD")) {
            throw new IllegalArgumentException("Payment method must be COD or DUMMY_CARD");
        }
        if (normalizedMethod.equals("DUMMY_CARD") && (cardLastFour == null || !cardLastFour.matches("\\d{4}"))) {
            throw new IllegalArgumentException("Dummy card payment requires the last four card digits");
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        Payment payment = paymentRepository.findByOrderId(orderId).stream().findFirst().orElseGet(Payment::new);
        if (payment.getPaymentId() == null) payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setAmount(order.getTotalAmount());
        payment.setMethod(normalizedMethod);
        payment.setTransactionRef(normalizedMethod.equals("DUMMY_CARD") ? "DUMMY-" + cardLastFour : "COD-" + orderId.toString().substring(0, 8));
        payment.setStatus(normalizedMethod.equals("DUMMY_CARD") ? "PAID" : "PENDING");
        if (normalizedMethod.equals("DUMMY_CARD")) payment.setPaidAt(LocalDateTime.now());

        order.setPaymentStatus(payment.getStatus());
        if (normalizedMethod.equals("DUMMY_CARD")) order.setStatus("CONFIRMED");
        orderRepository.save(order);
        return paymentRepository.save(payment);
    }
}
