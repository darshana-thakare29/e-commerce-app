package com.example.demo.controller;

import com.example.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    @WithMockUser
    void testGetProduct_success() throws Exception {

        UUID id = UUID.randomUUID();

        when(productService.getById(id)).thenReturn(new com.example.demo.entity.Product());

        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetProduct_withData() throws Exception {

        UUID id = UUID.randomUUID();

        when(productService.getById(id)).thenReturn(new com.example.demo.entity.Product());

        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetProduct_invalidUUID() throws Exception {

        mockMvc.perform(get("/api/products/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testGetFeaturedProducts_success() throws Exception {

        when(productService.getFeaturedProducts()).thenReturn(List.of());

        mockMvc.perform(get("/api/products/featured"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetFeaturedProducts_withData() throws Exception {

        when(productService.getFeaturedProducts())
                .thenReturn(List.of(new com.example.demo.entity.Product()));

        mockMvc.perform(get("/api/products/featured"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetProduct_unauthorized() throws Exception {

        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testGetFeaturedProducts_unauthorized() throws Exception {

        mockMvc.perform(get("/api/products/featured"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetProduct_contentType() throws Exception {

        UUID id = UUID.randomUUID();

        when(productService.getById(id)).thenReturn(new com.example.demo.entity.Product());

        mockMvc.perform(get("/api/products/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    @WithMockUser
    void testGetFeaturedProducts_contentType() throws Exception {

        when(productService.getFeaturedProducts()).thenReturn(List.of());

        mockMvc.perform(get("/api/products/featured"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }
}