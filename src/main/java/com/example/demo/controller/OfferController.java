package com.example.demo.controller;

import com.example.demo.dto.OfferRequest;
import com.example.demo.dto.OfferResponse;
import com.example.demo.service.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/offers")
public class OfferController {

    private final OfferService offerService;

    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping
    public ResponseEntity<OfferResponse> createOffer(
            @RequestBody OfferRequest request
    ) {
        return ResponseEntity.ok(
                offerService.createOffer(request)
        );
    }

    @GetMapping
    public ResponseEntity<List<OfferResponse>> getAllOffers() {
        return ResponseEntity.ok(
                offerService.getAllOffers()
        );
    }

    @GetMapping("/{offerId}")
    public ResponseEntity<OfferResponse> getOfferById(
            @PathVariable UUID offerId
    ) {
        return ResponseEntity.ok(
                offerService.getOfferById(offerId)
        );
    }

    @PutMapping("/{offerId}")
    public ResponseEntity<OfferResponse> updateOffer(
            @PathVariable UUID offerId,
            @RequestBody OfferRequest request
    ) {
        return ResponseEntity.ok(
                offerService.updateOffer(offerId, request)
        );
    }

    @DeleteMapping("/{offerId}")
    public ResponseEntity<String> deleteOffer(
            @PathVariable UUID offerId
    ) {
        offerService.deleteOffer(offerId);
        return ResponseEntity.ok("Offer deleted successfully");
    }
}
