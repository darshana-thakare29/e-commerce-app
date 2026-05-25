package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.document.ProductDocument;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductSearchRepository;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSearchRepository productSearchRepository;

    public Product saveProduct(Product product) {

        Product saved = productRepository.save(product);

        ProductDocument doc = new ProductDocument();
        doc.setId(saved.getProductId().toString());
        doc.setName(saved.getName());
        doc.setBrand(saved.getBrand());
        doc.setPrice(saved.getBasePrice());
        doc.setDescription(saved.getDescription());

        productSearchRepository.save(doc);

        return saved;
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findTop10ByStatus("Active");
    }

    public Product getById(UUID id) {
        return productRepository.findById(id).orElseThrow();
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

    public List<ProductDocument> searchProducts(String keyword) {
        return productSearchRepository.findByNameContaining(keyword);
    }
}