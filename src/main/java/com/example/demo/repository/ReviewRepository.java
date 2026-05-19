package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Review;

public interface ReviewRepository
extends JpaRepository<Review, UUID> {

List<Review> findTop5ByStatusOrderByCreatedAtDesc(String status);
}
