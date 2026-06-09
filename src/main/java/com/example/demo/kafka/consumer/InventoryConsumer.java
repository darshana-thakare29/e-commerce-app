package com.example.demo.kafka.consumer;

import com.example.demo.entity.Inventory;
import com.example.demo.kafka.event.OrderEvent;
import com.example.demo.kafka.topic.TopicNames;
import com.example.demo.repository.InventoryRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Service
public class InventoryConsumer {

    private static final Logger log = LoggerFactory.getLogger(InventoryConsumer.class);

    private final InventoryRepository inventoryRepository;

    public InventoryConsumer(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }
    @KafkaListener(topics = TopicNames.ORDER_TOPIC)
    @org.springframework.retry.annotation.Retryable(
            value = Exception.class,
            maxAttempts = 3,
            backoff = @org.springframework.retry.annotation.Backoff(delay = 2000)
    )
    public void consume(OrderEvent event) {

        try {
            log.info("Inventory check for Order: {}", event.getOrderId());

            Optional<Inventory> optionalInventory =
                    inventoryRepository.findByVariantId(
                            java.util.UUID.fromString(event.getVariantId())
                    );

            if (optionalInventory.isPresent()) {

                Inventory inventory = optionalInventory.get();

                inventory.setStockQuantity(inventory.getStockQuantity() - 1);

                inventoryRepository.save(inventory);

                log.info("Inventory updated for Order: {}", event.getOrderId());
            }
        } catch (Exception e){
            log.error("Failed to process order after retries: {}", event.getOrderId());
            throw e;
        }
    }
}