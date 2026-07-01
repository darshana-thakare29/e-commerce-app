package com.example.demo.service;

import com.example.demo.dto.OfferCategoryRequest;
import com.example.demo.dto.OfferCategoryResponse;
import com.example.demo.entity.OfferCategory;
import com.example.demo.repository.OfferCategoryRepository;
import com.example.demo.service.OfferCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OfferCategoryServiceImpl implements OfferCategoryService {

    private final OfferCategoryRepository offerCategoryRepository;

    public OfferCategoryServiceImpl(OfferCategoryRepository offerCategoryRepository) {
        this.offerCategoryRepository = offerCategoryRepository;
    }

    @Override
    public OfferCategoryResponse createOfferCategory(OfferCategoryRequest request) {
        OfferCategory offerCategory = new OfferCategory();
        offerCategory.setTitle(request.getTitle());
        offerCategory.setDescription(request.getDescription());
        offerCategory.setStatus(request.isStatus());

        OfferCategory saved = offerCategoryRepository.save(offerCategory);

        return mapToResponse(saved);
    }

    @Override
    public List<OfferCategoryResponse> getAllOfferCategories() {
        return offerCategoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OfferCategoryResponse getOfferCategoryById(UUID offerCategoryId) {
        OfferCategory offerCategory = offerCategoryRepository.findById(offerCategoryId)
                .orElseThrow(() -> new RuntimeException("Offer category not found"));

        return mapToResponse(offerCategory);
    }

    @Override
    public OfferCategoryResponse updateOfferCategory(UUID offerCategoryId, OfferCategoryRequest request) {
        OfferCategory offerCategory = offerCategoryRepository.findById(offerCategoryId)
                .orElseThrow(() -> new RuntimeException("Offer category not found"));

        offerCategory.setTitle(request.getTitle());
        offerCategory.setDescription(request.getDescription());
        offerCategory.setStatus(request.isStatus());

        OfferCategory updated = offerCategoryRepository.save(offerCategory);

        return mapToResponse(updated);
    }

    @Override
    public void deleteOfferCategory(UUID offerCategoryId) {
        OfferCategory offerCategory = offerCategoryRepository.findById(offerCategoryId)
                .orElseThrow(() -> new RuntimeException("Offer category not found"));

        offerCategoryRepository.delete(offerCategory);
    }

    private OfferCategoryResponse mapToResponse(OfferCategory offerCategory) {
        OfferCategoryResponse response = new OfferCategoryResponse();
        response.setOfferCategoryId(offerCategory.getOfferCategoryId());
        response.setTitle(offerCategory.getTitle());
        response.setDescription(offerCategory.getDescription());
        response.setStatus(offerCategory.isStatus());

        return response;
    }
}