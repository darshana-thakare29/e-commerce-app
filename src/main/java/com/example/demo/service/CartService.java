package com.example.demo.service;

import java.util.List;
import java.util.UUID;

import com.example.demo.dto.CartItemView;
import com.example.demo.dto.CartTotals;
import com.example.demo.dto.CartValidationResult;
import com.example.demo.dto.CartView;
import com.example.demo.dto.AddCartItemRequest;
import com.example.demo.dto.SetCartItemRequest;

public interface CartService {

    CartView getCart(UUID userId);

    UUID getOrCreateCartId(UUID userId);

    boolean cartExists(UUID userId);

    void clearCart(UUID userId);

    CartView addItem(UUID userId, UUID variantId, int quantity);

    CartView addItems(UUID userId, List<AddCartItemRequest> items);

    CartView updateItemQuantity(UUID userId, UUID cartItemId, int quantity);

    CartView setItemQuantity(UUID userId, UUID variantId, int quantity);

    CartView removeItem(UUID userId, UUID cartItemId);

    CartView removeItemByVariant(UUID userId, UUID variantId);

    CartView replaceAllItems(UUID userId, List<SetCartItemRequest> items);

    List<CartItemView> listItems(UUID userId);

    CartValidationResult validateCart(UUID userId);

    CartValidationResult validateCartStock(UUID userId);

    CartValidationResult validateCartStock(UUID userId, UUID warehouseId);

    CartTotals getTotals(UUID userId);

    CartView refreshCart(UUID userId);
}