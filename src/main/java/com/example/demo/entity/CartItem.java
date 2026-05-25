package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cart_item")
public class CartItem {

    @Id
    private UUID cartItemId;

    private UUID cartId;
    private UUID variantId;

    private Integer quantity;

    private LocalDateTime addedAt;


    public UUID getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(UUID cartItemId) {
        this.cartItemId = cartItemId;
    }

    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
    }

    public UUID getVariantId() {
        return variantId;
    }

    public void setVariantId(UUID variantId) {
        this.variantId = variantId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public void setAddedAt(LocalDateTime now) {
    }
}