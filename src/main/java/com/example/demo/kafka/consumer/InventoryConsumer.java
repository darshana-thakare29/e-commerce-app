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
    public void consume(OrderEvent event) {

        UUID orderId = UUID.fromString(event.getOrderId());

        List<OrderItem> items = orderItemRepository.findByOrderId(orderId);

        switch (event.getStatus()) {

            // ORDER CREATED → RESERVE STOCK
            case "ORDER_CREATED":

                for (OrderItem item : items) {

                    Inventory inventory = inventoryRepository
                            .findByVariantId(item.getVariantId())
                            .orElseThrow(() -> new RuntimeException("Inventory not found"));

                    if (inventory.getStockQuantity() < item.getQuantity()) {
                        throw new RuntimeException("Out of stock: " + item.getVariantId());
                    }

                    inventory.setReservedQuantity(
                            inventory.getReservedQuantity() + item.getQuantity()
                    );

                    inventoryRepository.save(inventory);
                }

                log.info("Stock RESERVED for order: {}", orderId);
                break;

            // PAYMENT SUCCESS → FINALIZE STOCK
            case "PAYMENT_SUCCESS":

                for (OrderItem item : items) {

                    Inventory inventory = inventoryRepository
                            .findByVariantId(item.getVariantId())
                            .orElseThrow(() -> new RuntimeException("Inventory not found"));

                    inventory.setStockQuantity(
                            inventory.getStockQuantity() - item.getQuantity()
                    );

                    inventory.setReservedQuantity(
                            inventory.getReservedQuantity() - item.getQuantity()
                    );

                    inventoryRepository.save(inventory);
                }

                log.info("Stock CONFIRMED for order: {}", orderId);
                break;

            // PAYMENT FAILED / CANCELLED → RELEASE STOCK
            case "PAYMENT_FAILED":
            case "ORDER_CANCELLED":

                for (OrderItem item : items) {

                    Inventory inventory = inventoryRepository
                            .findByVariantId(item.getVariantId())
                            .orElseThrow(() -> new RuntimeException("Inventory not found"));

                    inventory.setReservedQuantity(
                            inventory.getReservedQuantity() - item.getQuantity()
                    );

                    inventoryRepository.save(inventory);
                }

                log.info("Stock RELEASED for order: {}", orderId);
                break;

            default:
                log.warn("Unknown event type: {}", event.getStatus());
                throw new RuntimeException("Unknown event type: " + event.getStatus());
        }
    }
}