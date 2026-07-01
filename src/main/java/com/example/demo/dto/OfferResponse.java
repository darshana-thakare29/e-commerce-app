package com.example.demo.dto;

import java.util.UUID;

public class OfferResponse {
    private UUID offerId;
    private UUID offerCategoryId;
    private String offerCategoryTitle;
    private String title;
    private String description;
    private boolean status;

    public UUID getOfferId() {
        return offerId;
    }

    public void setOfferId(UUID offerId) {
        this.offerId = offerId;
    }

    public UUID getOfferCategoryId() {
        return offerCategoryId;
    }

    public void setOfferCategoryId(UUID offerCategoryId) {
        this.offerCategoryId = offerCategoryId;
    }

    public String getOfferCategoryTitle() {
        return offerCategoryTitle;
    }

    public void setOfferCategoryTitle(String offerCategoryTitle) {
        this.offerCategoryTitle = offerCategoryTitle;
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
