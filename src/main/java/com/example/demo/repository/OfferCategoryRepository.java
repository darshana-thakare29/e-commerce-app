package com.example.demo.repository;

import com.example.demo.entity.OfferCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OfferCategoryRepository extends JpaRepository<OfferCategory, UUID> {
}