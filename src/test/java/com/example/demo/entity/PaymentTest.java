package com.example.demo.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testPayment_allFieldsComplete() {

        Payment payment = new Payment();

        UUID paymentId = UUID.randomUUID();
        UUID orderId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        BigDecimal amount = new BigDecimal("500.25");
        LocalDateTime paidAt = LocalDateTime.now();

        payment.setPaymentId(paymentId);
        payment.setOrderId(orderId);
        payment.setUserId(userId);
        payment.setAmount(amount);
        payment.setMethod("UPI");
        payment.setStatus("SUCCESS");
        payment.setTransactionRef("TXN123");
        payment.setPaidAt(paidAt);

        assertEquals(paymentId, payment.getPaymentId());
        assertEquals(orderId, payment.getOrderId());
        assertEquals(userId, payment.getUserId());
        assertEquals(amount, payment.getAmount());
        assertEquals("UPI", payment.getMethod());
        assertEquals("SUCCESS", payment.getStatus());
        assertEquals("TXN123", payment.getTransactionRef());
        assertEquals(paidAt, payment.getPaidAt());
    }

    @Test
    void testPayment_nullValues() {

        Payment payment = new Payment();

        payment.setOrderId(null);
        payment.setUserId(null);
        payment.setAmount(null);
        payment.setMethod(null);
        payment.setStatus(null);
        payment.setTransactionRef(null);

        assertNull(payment.getOrderId());
        assertNull(payment.getUserId());
        assertNull(payment.getAmount());
        assertNull(payment.getMethod());
        assertNull(payment.getStatus());
        assertNull(payment.getTransactionRef());
    }

    @Test
    void testPayment_multipleUpdates() {

        Payment payment = new Payment();

        UUID first = UUID.randomUUID();
        UUID second = UUID.randomUUID();

        payment.setOrderId(first);
        assertEquals(first, payment.getOrderId());

        payment.setOrderId(second);
        assertEquals(second, payment.getOrderId());
    }

    @Test
    void testPayment_statusAndMethodUpdate() {

        Payment payment = new Payment();

        payment.setStatus("PENDING");
        assertEquals("PENDING", payment.getStatus());

        payment.setStatus("FAILED");
        assertEquals("FAILED", payment.getStatus());

        payment.setMethod("CARD");
        assertEquals("CARD", payment.getMethod());

        payment.setMethod("NETBANKING");
        assertEquals("NETBANKING", payment.getMethod());
    }

    @Test
    void testPayment_amountEdge() {

        Payment payment = new Payment();

        payment.setAmount(BigDecimal.ZERO);
        assertEquals(BigDecimal.ZERO, payment.getAmount());

        payment.setAmount(new BigDecimal("999999"));
        assertEquals(new BigDecimal("999999"), payment.getAmount());
    }

    @Test
    void testPayment_constructor() {

        Payment payment = new Payment();

        assertNotNull(payment);
    }

}