package com.example.demo.kafka.consumer;

import com.example.demo.entity.Payment;
import com.example.demo.kafka.event.OrderEvent;
import com.example.demo.kafka.topic.TopicNames;
import com.example.demo.repository.PaymentRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentConsumer {

    private final PaymentRepository paymentRepository;
    private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);

    public PaymentConsumer(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }

    @KafkaListener(topics = TopicNames.ORDER_TOPIC, groupId = "payment-group")
    @org.springframework.retry.annotation.Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @org.springframework.retry.annotation.Backoff(delay = 2000)
    )
    public void consume(OrderEvent event) {

        Payment payment = new Payment();        payment.setPaymentId(UUID.randomUUID());
        payment.setOrderId(UUID.fromString(event.getOrderId()));
        payment.setUserId(UUID.fromString(event.getUserId()));
        payment.setAmount(BigDecimal.valueOf(event.getAmount()));
        payment.setStatus("PENDING");

        paymentRepository.save(payment);

        System.out.println("Payment created for order: " + event.getOrderId());

        log.info("Payment created for order: {}", event.getOrderId());
    }

}