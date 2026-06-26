package com.example.demo.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Product;

public interface ProductRepository
extends JpaRepository<Product, UUID> {

List<Product> findTop10ByStatus(String status);
List<Product> findByCategory_CategoryId(UUID categoryId);
List<Product> findByNameContainingIgnoreCase(String name);
}