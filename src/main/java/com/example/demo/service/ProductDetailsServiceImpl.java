package com.example.demo.service;

import com.example.demo.dto.ProductDetailsRequest;
import com.example.demo.dto.ProductDetailsView;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetails;
import com.example.demo.repository.ProductDetailsRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductDetailsServiceImpl implements ProductDetailsService {

    private final ProductDetailsRepository productDetailsRepository;
    private final ProductRepository productRepository;

    public ProductDetailsServiceImpl(ProductDetailsRepository productDetailsRepository,
                                     ProductRepository productRepository) {
        this.productDetailsRepository = productDetailsRepository;
        this.productRepository = productRepository;
    }

    @Override
    public ProductDetailsView addProductDetails(UUID productId, ProductDetails request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + productId));

        if (productDetailsRepository.existsBySku(request.getSku())) {
            throw new RuntimeException("SKU already exists: " + request.getSku());
        }

        request.setProduct(product);

        ProductDetails saved = productDetailsRepository.save(request);

        return mapToView(saved);
    }

    @Override
    public ProductDetailsView getProductDetailsById(UUID variantId) {

        ProductDetails variant = productDetailsRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found with id: " + variantId));

        return mapToView(variant);
    }

    @Override
    public List<ProductDetailsView> getAllProductDetailsByProductId(UUID productId) {

        List<ProductDetails> variants =
                productDetailsRepository.findByProductProductId(productId);

        return variants.stream()
                .map(this::mapToView)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDetailsView updateProductDetails(UUID variantId, ProductDetailsRequest request){

        ProductDetails existing = productDetailsRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found with id: " + variantId));

        existing.setColor(request.getColor());
        existing.setSize(request.getSize());
        existing.setPrice(request.getPrice());
        existing.setWeight(request.getWeight());
        existing.setStatus(request.getStatus());

        // SKU update check
        if (!existing.getSku().equals(request.getSku())) {
            if (productDetailsRepository.existsBySku(request.getSku())) {
                throw new RuntimeException("SKU already exists: " + request.getSku());
            }
            existing.setSku(request.getSku());
        }

        ProductDetails updated = productDetailsRepository.save(existing);

        return mapToView(updated);
    }

    @Override
    public void deleteProductDetails(UUID variantId) {

        ProductDetails existing = productDetailsRepository.findById(variantId)
                .orElseThrow(() -> new RuntimeException("Variant not found with id: " + variantId));

        productDetailsRepository.delete(existing);
    }

    private ProductDetailsView mapToView(ProductDetails productDetails) {

        return new ProductDetailsView(
            productDetails.getVariantId(),
            productDetails.getProduct().getProductId(),
            productDetails.getSku(),
            productDetails.getColor(),
            productDetails.getSize(),
            productDetails.getPrice(),
            productDetails.getWeight(),
            productDetails.getStatus(),
            productDetails.getImageUrls()
        );
    }
}