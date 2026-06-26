package com.example.demo.repository;

import com.example.demo.entity.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductDetailsRepository extends JpaRepository<ProductDetails, UUID> {

    List<ProductDetails> findByProductProductId(UUID productId);

    boolean existsBySku(String sku);

}