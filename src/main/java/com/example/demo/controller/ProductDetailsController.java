package com.example.demo.controller;

import com.example.demo.dto.ProductDetailsRequest;
import com.example.demo.dto.ProductDetailsResponse;
import com.example.demo.dto.ProductDetailsView;
import com.example.demo.dto.ReviewView;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetails;
import com.example.demo.service.ProductDetailsService;
import com.example.demo.service.ProductService;
import com.example.demo.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductDetailsController {

    @Autowired
    private ProductDetailsService productDetailsService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReviewService reviewService;

    // Add Product Variant (Product Details)
    @PostMapping("/{productId}/variants")
    public ResponseEntity<ProductDetailsView> addProductDetails(
            @PathVariable UUID productId,
            @RequestBody ProductDetails productDetails) {

        ProductDetailsView response =
                productDetailsService.addProductDetails(productId, productDetails);

        return ResponseEntity.ok(response);
    }

    // Get all variants of a product
    @GetMapping("/{productId}/variants")
    public ResponseEntity<List<ProductDetailsView>> getAllVariants(
            @PathVariable UUID productId) {

        return ResponseEntity.ok(
                productDetailsService.getAllProductDetailsByProductId(productId)
        );
    }

    // Get single variant
    @GetMapping("/variants/{variantId}")
    public ResponseEntity<ProductDetailsView> getVariantById(
            @PathVariable UUID variantId) {

        return ResponseEntity.ok(
                productDetailsService.getProductDetailsById(variantId)
        );
    }

    // Get detailed product data including variants and reviews
    @GetMapping("/{productId}/details")
    public ResponseEntity<ProductDetailsResponse> getProductDetails(
            @PathVariable UUID productId) {

        Product product = productService.getById(productId);
        List<ProductDetailsView> variants =
                productDetailsService.getAllProductDetailsByProductId(productId);
        List<ReviewView> reviews =
                reviewService.getReviewsByProductId(productId);

        ProductDetailsResponse response = new ProductDetailsResponse(
                product.getProductId(),
                product.getName(),
                product.getBrand(),
                product.getDescription(),
                product.getBasePrice(),
                product.getStatus(),
                variants,
                reviews
        );

        return ResponseEntity.ok(response);
    }

    // Update variant
    @PutMapping("/variants/{variantId}")
    public ResponseEntity<ProductDetailsView> updateVariant(
            @PathVariable UUID variantId,
            @RequestBody ProductDetailsRequest request) {

        return ResponseEntity.ok(
                productDetailsService.updateProductDetails(variantId, request)
        );
    }

    // Delete variant
    @DeleteMapping("/variants/{variantId}")
    public ResponseEntity<String> deleteVariant(
            @PathVariable UUID variantId) {

        productDetailsService.deleteProductDetails(variantId);
        return ResponseEntity.ok("Variant deleted successfully");
    }
}