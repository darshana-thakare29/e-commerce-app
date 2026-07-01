package com.example.demo.service;

import com.example.demo.dto.OfferRequest;
import com.example.demo.dto.OfferResponse;

import java.util.List;
import java.util.UUID;

public interface OfferService {

    OfferResponse createOffer(OfferRequest request);

    List<OfferResponse> getAllOffers();

    OfferResponse getOfferById(UUID offerId);

    OfferResponse updateOffer(UUID offerId, OfferRequest request);

    void deleteOffer(UUID offerId);
}
