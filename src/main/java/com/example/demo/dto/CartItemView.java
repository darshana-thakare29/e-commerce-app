package com.example.demo.dto;

import java.util.UUID;
import java.math.BigDecimal;

public class CartItemView {

    private UUID cartItemId;
    private UUID variantId;
    private UUID productId;
    private String productName;
    private String brand;
    private BigDecimal price;
    private Integer quantity;

    public CartItemView() {
    }

    public UUID getVariantId() {
        return variantId;
    }

    public void setVariantId(UUID variantId) {
        this.variantId = variantId;
    }

    public UUID getCartItemId() { return cartItemId; }
    public void setCartItemId(UUID cartItemId) { this.cartItemId = cartItemId; }
    public UUID getProductId() { return productId; }
    public void setProductId(UUID productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
