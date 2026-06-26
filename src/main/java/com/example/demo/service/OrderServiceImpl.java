package com.example.demo.service;

import com.example.demo.dto.ApiResponse;
import com.example.demo.dto.CheckoutRequest;
import com.example.demo.entity.*;
import com.example.demo.kafka.event.OrderEvent;
import com.example.demo.kafka.producer.OrderProducer;
import com.example.demo.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AddressRepository addressRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductDetailsRepository productDetailsRepository;


    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            CartRepository cartRepository,
                            CartItemRepository cartItemRepository,
                            AddressRepository addressRepository,
                            WarehouseRepository warehouseRepository,
                            ProductDetailsRepository productDetailsRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.addressRepository = addressRepository;
        this.warehouseRepository = warehouseRepository;
        this.productDetailsRepository = productDetailsRepository;
    }

    @Autowired
    private OrderProducer orderProducer;
    @Override
    public ApiResponse<UUID> checkout(UUID userId, UUID addressId, UUID warehouseId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getCartId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setOrderId(UUID.randomUUID());
        order.setUserId(userId);
        order.setAddressId(addressId);
        order.setWarehouseId(warehouseId);
        order.setStatus("PENDING");
        order.setPaymentStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;

        for (CartItem item : cartItems) {

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderItemId(UUID.randomUUID());
            orderItem.setOrderId(order.getOrderId());
            orderItem.setVariantId(item.getVariantId());
            orderItem.setQuantity(item.getQuantity());

            BigDecimal price = productDetailsRepository.findById(item.getVariantId())
                    .map(ProductDetails::getPrice)
                    .orElseThrow(() -> new RuntimeException("Product variant not found"));
            orderItem.setPrice(price);

            total = total.add(price.multiply(BigDecimal.valueOf(item.getQuantity())));

            orderItemRepository.save(orderItem);
        }

        order.setTotalAmount(total);
        orderRepository.save(order);


        OrderEvent event = new OrderEvent(
                order.getOrderId().toString(),
                userId.toString(),
                total.doubleValue(),
                "CREATED"
        );

        orderProducer.sendOrder(event);

        cartItemRepository.deleteAll(cartItems);

        return new ApiResponse<>(
                true,
                "Order placed successfully",
                order.getOrderId()
        );
    }

    @Override
    public ApiResponse<UUID> checkout(UUID userId, CheckoutRequest request) {
        UUID addressId = request.getAddressId();
        if (addressId == null) {
            addressId = addressRepository.findByUserId(userId).stream()
                    .findFirst().map(Address::getAddressId)
                    .orElseGet(() -> createCheckoutAddress(userId));
        }

        UUID warehouseId = request.getWarehouseId();
        if (warehouseId == null) {
            warehouseId = warehouseRepository.findAll().stream()
                    .findFirst().map(Warehouse::getWarehouseId)
                    .orElseGet(this::createDefaultWarehouse);
        }
        return checkout(userId, addressId, warehouseId);
    }

    private UUID createCheckoutAddress(UUID userId) {
        Address address = new Address();
        address.setAddressId(UUID.randomUUID());
        address.setUserId(userId);
        address.setName("Online Customer");
        address.setAddressLine("Address to be confirmed");
        address.setCity("Online");
        address.setState("Online");
        address.setPincode(0);
        address.setType("CHECKOUT");
        return addressRepository.save(address).getAddressId();
    }

    private UUID createDefaultWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseId(UUID.nameUUIDFromBytes("default-checkout-warehouse".getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        warehouse.setName("Default Warehouse");
        warehouse.setCity("Online");
        warehouse.setState("Online");
        warehouse.setStatus("ACTIVE");
        return warehouseRepository.save(warehouse).getWarehouseId();
    }

    @Override
    public Object getOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Object> getUserOrders(UUID userId) {
        return (List) orderRepository.findByUserId(userId);
    }

    @Override
    public void cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }
}

