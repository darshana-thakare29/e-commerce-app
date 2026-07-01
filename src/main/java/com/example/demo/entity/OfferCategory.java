package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "offer_category")
public class OfferCategory {

    @Id
    @GeneratedValue
    @Column(name = "offer_category_id")
    private UUID offerCategoryId;

    private String title;

    @Column(columnDefinition = "TEXT")
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