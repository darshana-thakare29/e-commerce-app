package com.example.demo.controller;

import com.example.demo.dto.OfferCategoryRequest;
import com.example.demo.dto.OfferCategoryResponse;
import com.example.demo.service.OfferCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/offer-categories")
public class OfferCategoryController {

    private final OfferCategoryService offerCategoryService;

    public OfferCategoryController(OfferCategoryService offerCategoryService) {
        this.offerCategoryService = offerCategoryService;
    }

    @PostMapping
    public ResponseEntity<OfferCategoryResponse> createOfferCategory(
            @RequestBody OfferCategoryRequest request
    ) {
        return ResponseEntity.ok(
                offerCategoryService.createOfferCategory(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<OfferCategoryResponse>> getAllOfferCategories() {
        return ResponseEntity.ok(
                offerCategoryService.getAllOfferCategories()
        );
    }

    @GetMapping("/{offerCategoryId}")
    public ResponseEntity<OfferCategoryResponse> getOfferCategoryById(
            @PathVariable UUID offerCategoryId
    ) {
        return ResponseEntity.ok(
                offerCategoryService.getOfferCategoryById(offerCategoryId)
        );
    }

    @PutMapping("/{offerCategoryId}")
    public ResponseEntity<OfferCategoryResponse> updateOfferCategory(
            @PathVariable UUID offerCategoryId,
            @RequestBody OfferCategoryRequest request
    ) {
        return ResponseEntity.ok(
                offerCategoryService.updateOfferCategory(offerCategoryId, request)
        );
    }

    @DeleteMapping("/{offerCategoryId}")
    public ResponseEntity<String> deleteOfferCategory(
            @PathVariable UUID offerCategoryId
    ) {
        offerCategoryService.deleteOfferCategory(offerCategoryId);
        return ResponseEntity.ok("Offer category deleted successfully");
    }
}