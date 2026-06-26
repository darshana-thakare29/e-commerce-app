package com.example.demo.dto;

import java.util.List;
import java.util.UUID;

public class ProductDetailsResponse {

    private UUID productId;
    private String name;
    private String brand;
    private String description;
    private Double basePrice;
    private String status;
    private List<ProductDetailsView> variants;
    private List<ReviewView> reviews;

    public ProductDetailsResponse() {
    }

    public ProductDetailsResponse(UUID productId, String name, String brand, String description, Double basePrice, String status, List<ProductDetailsView> variants, List<ReviewView> reviews) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.basePrice = basePrice;
        this.status = status;
        this.variants = variants;
        this.reviews = reviews;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductDetailsView> getVariants() {
        return variants;
    }

    public void setVariants(List<ProductDetailsView> variants) {
        this.variants = variants;
    }

    public List<ReviewView> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewView> reviews) {
        this.reviews = reviews;
    }
}