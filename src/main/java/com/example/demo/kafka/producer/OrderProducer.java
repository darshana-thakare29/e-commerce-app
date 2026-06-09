package com.example.demo.kafka.producer;

import com.example.demo.kafka.event.OrderEvent;
import com.example.demo.kafka.topic.TopicNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrder(OrderEvent event) {
        kafkaTemplate.send(TopicNames.ORDER_TOPIC, event);
        System.out.println("Event sent: " + event.getOrderId());
    }
}