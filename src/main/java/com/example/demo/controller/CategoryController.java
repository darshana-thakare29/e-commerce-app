package com.example.demo.controller;

import com.example.demo.dto.ApiResponse;
import com.example.demo.entity.Category;
import com.example.demo.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/products/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ApiResponse<List<Category>> getAllCategories() {
        return categoryService.getAllCategories();
    }
}
