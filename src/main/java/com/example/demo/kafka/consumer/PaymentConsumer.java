package com.example.demo.kafka.consumer;

import com.example.demo.kafka.event.OrderEvent;
import com.example.demo.kafka.topic.TopicNames;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PaymentConsumer {

    private static final Logger log = LoggerFactory.getLogger(PaymentConsumer.class);

    @KafkaListener(topics = TopicNames.ORDER_TOPIC, groupId = "payment-group")
    @org.springframework.retry.annotation.Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @org.springframework.retry.annotation.Backoff(delay = 2000)
    )
    public void consume(OrderEvent event) {

        log.info("Payment processing for order: {}", event.getOrderId());
        log.info("Payment completed for order: {}", event.getOrderId());
    }
}