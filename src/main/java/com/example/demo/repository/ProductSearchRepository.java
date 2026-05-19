package com.example.demo.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.demo.document.ProductDocument;

public interface ProductSearchRepository
extends ElasticsearchRepository<ProductDocument, String> {

List<ProductDocument> findByNameContaining(String keyword);
}