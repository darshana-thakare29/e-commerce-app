package com.example.demo.service;

import com.example.demo.dto.ProductDetailsRequest;
import com.example.demo.dto.ProductDetailsView;
import com.example.demo.entity.ProductDetails;

import java.util.List;
import java.util.UUID;

public interface ProductDetailsService {

    ProductDetailsView addProductDetails(UUID productId, ProductDetails productDetails);

    ProductDetailsView getProductDetailsById(UUID variantId);

    List<ProductDetailsView> getAllProductDetailsByProductId(UUID productId);

    ProductDetailsView updateProductDetails(UUID variantId, ProductDetailsRequest request);

    void deleteProductDetails(UUID variantId);
}