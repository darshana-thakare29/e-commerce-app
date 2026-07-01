package com.example.demo.service;

import com.example.demo.dto.OfferCategoryRequest;
import com.example.demo.dto.OfferCategoryResponse;

import java.util.List;
import java.util.UUID;

public interface OfferCategoryService {

    OfferCategoryResponse createOfferCategory(OfferCategoryRequest request);

    List<OfferCategoryResponse> getAllOfferCategories();

    OfferCategoryResponse getOfferCategoryById(UUID offerCategoryId);

    OfferCategoryResponse updateOfferCategory(UUID offerCategoryId, OfferCategoryRequest request);

    void deleteOfferCategory(UUID offerCategoryId);
}