package com.example.demo.service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ProductSummaryView;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductDetails;
import com.example.demo.repository.ProductDetailsRepository;
import com.example.demo.repository.ProductRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    

    @Autowired
    private ProductDetailsRepository productDetailsRepository;

    public Product saveProduct(Product product) {

        Product saved = productRepository.save(product);
        return saved;
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findTop10ByStatus("Active");
    }

    public Product getById(UUID id) {
        return productRepository.findById(id).orElseThrow();
    }

    public List<ProductSummaryView> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToSummary)
                .collect(Collectors.toList());
    }

    public ProductSummaryView getProductSummaryById(UUID id) {
        Product product = getById(id);
        return mapToSummary(product);
    }

    private ProductSummaryView mapToSummary(Product product) {
        String imageUrl = findThumbnail(product);

        return new ProductSummaryView(
            product.getProductId(),
            product.getName(),
            product.getBrand(),
            product.getBasePrice(),
            product.getDescription(),
            imageUrl
        );
    }

    private String findThumbnail(Product product) {
        List<ProductDetails> variants =
                productDetailsRepository.findByProductProductId(product.getProductId());

        if (!variants.isEmpty() && variants.get(0).getImageUrls() != null && !variants.get(0).getImageUrls().isEmpty()) {
            return variants.get(0).getImageUrls().get(0);
        }

        return null;
    }

    public Product updateProduct(UUID id, Product updatedProduct) {

        Product existing = productRepository.findById(id)
                .orElseThrow();

        existing.setName(updatedProduct.getName());
        existing.setBrand(updatedProduct.getBrand());
        existing.setBasePrice(updatedProduct.getBasePrice());
        existing.setDescription(updatedProduct.getDescription());
        existing.setStatus(updatedProduct.getStatus());
        existing.setCategory(updatedProduct.getCategory());

        return productRepository.save(existing);
    }

    public void deleteProduct(UUID id) {
        productRepository.deleteById(id);
    }

    public List<Product> getProductsByCategory(UUID categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId);
    }
    public List<ProductSummaryView> getRandomProducts(int limit) {

        List<ProductSummaryView> products = getAllProducts();

        Collections.shuffle(products);

        return products.stream()
                .limit(limit)
                .toList();
    }
}