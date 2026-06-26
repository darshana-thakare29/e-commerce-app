package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class ReviewView {

    private UUID reviewId;
    private String reviewerName;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public ReviewView() {
    }

    public ReviewView(UUID reviewId, String reviewerName, int rating, String comment, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.reviewerName = reviewerName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public UUID getReviewId() {
        return reviewId;
    }

    public void setReviewId(UUID reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}