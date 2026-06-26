package com.example.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.demo.entity.Review;
import com.example.demo.service.ReviewService;
import com.example.demo.dto.ReviewView;


@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public Review add(@RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @GetMapping("/top")
    public List<ReviewView> topReviews() {
        return reviewService.getTopReviews();
    }

    @GetMapping("/product/{productId}")
    public List<ReviewView> getReviewsByProduct(@PathVariable UUID productId) {
        return reviewService.getReviewsByProductId(productId);
    }
}