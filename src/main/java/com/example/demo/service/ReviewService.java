package com.example.demo.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ReviewView;
import com.example.demo.entity.Review;
import com.example.demo.repository.ReviewRepository;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public Review addReview(Review review) {
        review.setStatus("Pending");
        review.setCreatedAt(LocalDateTime.now());
        return reviewRepository.save(review);
    }

    public List<ReviewView> getTopReviews() {
        return reviewRepository
                .findTop5ByStatusOrderByCreatedAtDesc("Approved")
                .stream()
                .map(this::mapToView)
                .collect(Collectors.toList());
    }

    public List<ReviewView> getReviewsByProductId(UUID productId) {
        return reviewRepository
                .findByProductProductIdAndStatusOrderByCreatedAtDesc(productId, "Approved")
                .stream()
                .map(this::mapToView)
                .collect(Collectors.toList());
    }

    private ReviewView mapToView(Review review) {
        return new ReviewView(
            review.getReviewId(),
            review.getUser() != null ? review.getUser().getName() : null,
            review.getRating(),
            review.getComment(),
            review.getCreatedAt()
        );
    }
}