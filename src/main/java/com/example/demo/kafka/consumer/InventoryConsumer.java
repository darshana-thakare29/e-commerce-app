package com.example.demo.kafka.consumer;

import com.example.demo.entity.Inventory;
import com.example.demo.entity.OrderItem;
import com.example.demo.kafka.event.OrderEvent;
import com.example.demo.kafka.topic.TopicNames;
import com.example.demo.repository.InventoryRepository;
import com.example.demo.repository.OrderItemRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryConsumer.class);

    private final InventoryRepository inventoryRepository;
    private final OrderItemRepository orderItemRepository;

    public InventoryConsumer(InventoryRepository inventoryRepository,
                             OrderItemRepository orderItemRepository) {
        this.inventoryRepository = inventoryRepository;
        this.orderItemRepository = orderItemRepository;
    }
    @KafkaListener(topics = TopicNames.ORDER_TOPIC)
    @org.springframework.retry.annotation.Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @org.springframework.retry.annotation.Backoff(delay = 2000)
    )
    public void consume(OrderEvent event) {

        UUID orderId = UUID.fromString(event.getOrderId());

        List<OrderItem> items =
                orderItemRepository.findByOrderId(orderId);

        for (OrderItem item : items) {

            Inventory inventory =
                    inventoryRepository.findByVariantId(item.getVariantId())
                            .orElseThrow(() -> new RuntimeException("Inventory not found"));

            if (inventory.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Out of stock for variant: " + item.getVariantId());
            }

            inventory.setReservedQuantity(
                    inventory.getReservedQuantity() + item.getQuantity()
            );

            inventoryRepository.save(inventory);
        }

        System.out.println("Inventory updated for order: " + event.getOrderId());
    }
}