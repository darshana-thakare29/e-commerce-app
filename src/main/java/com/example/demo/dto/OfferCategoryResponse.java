package com.example.demo.dto;

import java.util.UUID;

public class OfferCategoryResponse {
    private UUID offerCategoryId;
    private String title;
    private String description;
    private boolean status;

    public UUID getOfferCategoryId() {
        return offerCategoryId;
    }

    public void setOfferCategoryId(UUID offerCategoryId) {
        this.offerCategoryId = offerCategoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
