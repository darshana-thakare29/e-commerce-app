package com.example.demo.controller;

import com.example.demo.dto.CartItemView;
import com.example.demo.dto.CartView;
import com.example.demo.service.CartService;
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

@WebMvcTest(CartController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    @WithMockUser
    void testGetCart_success() throws Exception {

        UUID userId = UUID.randomUUID();

        when(cartService.getCart(userId)).thenReturn(new CartView());

        mockMvc.perform(get("/cart")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testListItems_success() throws Exception {

        UUID userId = UUID.randomUUID();

        when(cartService.listItems(userId)).thenReturn(List.of());

        mockMvc.perform(get("/cart/items")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetCart_withData() throws Exception {

        UUID userId = UUID.randomUUID();

        when(cartService.getCart(userId)).thenReturn(new CartView());

        mockMvc.perform(get("/cart")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testListItems_withData() throws Exception {

        UUID userId = UUID.randomUUID();
        CartItemView item = new CartItemView();

        when(cartService.listItems(userId)).thenReturn(List.of(item));

        mockMvc.perform(get("/cart/items")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void testGetCart_missingParam() throws Exception {

        mockMvc.perform(get("/cart"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testListItems_missingParam() throws Exception {

        mockMvc.perform(get("/cart/items"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    void testInvalidUUID() throws Exception {

        mockMvc.perform(get("/cart")
                        .param("userId", "invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCart_unauthorized() throws Exception {

        UUID userId = UUID.randomUUID();

        mockMvc.perform(get("/cart")
                        .param("userId", userId.toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListItems_unauthorized() throws Exception {

        UUID userId = UUID.randomUUID();

        mockMvc.perform(get("/cart/items")
                        .param("userId", userId.toString()))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetCart_contentType() throws Exception {

        UUID userId = UUID.randomUUID();

        when(cartService.getCart(userId)).thenReturn(new CartView());

        mockMvc.perform(get("/cart")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }

    @Test
    @WithMockUser
    void testListItems_contentType() throws Exception {

        UUID userId = UUID.randomUUID();

        when(cartService.listItems(userId)).thenReturn(List.of());

        mockMvc.perform(get("/cart/items")
                        .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("application/json"));
    }
}