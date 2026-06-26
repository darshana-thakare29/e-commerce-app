package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ProductDetailsView {

    private UUID variantId;

    private UUID productId;

    private String sku;

    private String color;

    private String size;

    private BigDecimal price;

    private Double weight;

    private String status;

    private List<String> imageUrls;

    public ProductDetailsView() {
    }

    public ProductDetailsView(UUID variantId, UUID productId, String sku, String color, String size, BigDecimal price, Double weight, String status, List<String> imageUrls) {
        this.variantId = variantId;
        this.productId = productId;
        this.sku = sku;
        this.color = color;
        this.size = size;
        this.price = price;
        this.weight = weight;
        this.status = status;
        this.imageUrls = imageUrls;
    }

    public UUID getVariantId() {
        return variantId;
    }

    public void setVariantId(UUID variantId) {
        this.variantId = variantId;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}