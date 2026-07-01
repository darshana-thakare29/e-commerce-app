package com.example.demo.service;

import com.example.demo.dto.OfferRequest;
import com.example.demo.dto.OfferResponse;
import com.example.demo.entity.Offer;
import com.example.demo.entity.OfferCategory;
import com.example.demo.repository.OfferCategoryRepository;
import com.example.demo.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final OfferCategoryRepository offerCategoryRepository;

    public OfferServiceImpl(
            OfferRepository offerRepository,
            OfferCategoryRepository offerCategoryRepository
    ) {
        this.offerRepository = offerRepository;
        this.offerCategoryRepository = offerCategoryRepository;
    }

    @Override
    public OfferResponse createOffer(OfferRequest request) {
        OfferCategory offerCategory = getOfferCategory(request.getOfferCategoryId());

        Offer offer = new Offer();
        offer.setOfferCategory(offerCategory);
        offer.setTitle(request.getTitle());
        offer.setDescription(request.getDescription());
        offer.setStatus(request.isStatus());

        Offer saved = offerRepository.save(offer);

        return mapToResponse(saved);
    }

    @Override
    public List<OfferResponse> getAllOffers() {
        return offerRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OfferResponse getOfferById(UUID offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        return mapToResponse(offer);
    }

    @Override
    public OfferResponse updateOffer(UUID offerId, OfferRequest request) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));
        OfferCategory offerCategory = getOfferCategory(request.getOfferCategoryId());

        offer.setOfferCategory(offerCategory);
        offer.setTitle(request.getTitle());
        offer.setDescription(request.getDescription());
        offer.setStatus(request.isStatus());

        Offer updated = offerRepository.save(offer);

        return mapToResponse(updated);
    }

    @Override
    public void deleteOffer(UUID offerId) {
        Offer offer = offerRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        offerRepository.delete(offer);
    }

    private OfferCategory getOfferCategory(UUID offerCategoryId) {
        return offerCategoryRepository.findById(offerCategoryId)
                .orElseThrow(() -> new RuntimeException("Offer Category not found"));
    }

    private OfferResponse mapToResponse(Offer offer) {
        OfferResponse response = new OfferResponse();
        response.setOfferId(offer.getOfferId());
        response.setOfferCategoryId(offer.getOfferCategory().getOfferCategoryId());
        response.setOfferCategoryTitle(offer.getOfferCategory().getTitle());
        response.setTitle(offer.getTitle());
        response.setDescription(offer.getDescription());
        response.setStatus(offer.isStatus());

        return response;
    }
}
