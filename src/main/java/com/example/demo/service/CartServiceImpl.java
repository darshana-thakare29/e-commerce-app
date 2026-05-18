package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }

    @Override
    public UUID getOrCreateCartId(UUID userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setCartId(UUID.randomUUID());
                    newCart.setUserId(userId);
                    newCart.setCreatedAt(java.time.LocalDateTime.now());
                    newCart.setUpdatedAt(java.time.LocalDateTime.now());
                    return cartRepository.save(newCart);
                });

        return cart.getCartId();
    }
    @Override
    public CartView getCart(UUID userId) {
        Cart cart = getOrCreateCart(userId);
        // If your CartView has fields, map cart + items here.
        // For now, returning an empty CartView so compilation works with empty DTO classes.
        return new CartView();
    }
    @Override
    public boolean cartExists(UUID userId) {
        return cartRepository.findByUserId(userId).isPresent();
    }

    @Override
    public CartView addItem(UUID userId, UUID variantId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        Cart cart = getOrCreateCart(userId);

        List<CartItem> items = cartItemRepository.findByCartId(cart.getCartId());
        Optional<CartItem> existing = items.stream()
                .filter(i -> variantId.equals(i.getVariantId()))
                .findFirst();

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem item = new CartItem();
            item.setCartItemId(UUID.randomUUID());
            item.setCartId(cart.getCartId());
            item.setVariantId(variantId);
            item.setQuantity(quantity);
            item.setAddedAt(LocalDateTime.now());
            cartItemRepository.save(item);
        }

        touchCart(cart);
        return new CartView();
    }

    @Override
    public CartView addItems(UUID userId, List<AddCartItemRequest> items) {
        if (items == null || items.isEmpty()) {
            return getCart(userId);
        }
        for (AddCartItemRequest req : items) {
            if (req == null) continue;
            addItem(userId, req.variantId, req.quantity);
        }
        return getCart(userId);
    }

    @Override
    public CartView updateItemQuantity(UUID userId, UUID cartItemId, int quantity) {
        Cart cart = getOrCreateCart(userId);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        if (!cart.getCartId().equals(item.getCartId())) {
            throw new IllegalArgumentException("Cart item does not belong to this user cart");
        }

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }

        touchCart(cart);
        return new CartView();
    }

    @Override
    public CartView setItemQuantity(UUID userId, UUID variantId, int quantity) {
        Cart cart = getOrCreateCart(userId);

        List<CartItem> items = cartItemRepository.findByCartId(cart.getCartId());
        Optional<CartItem> existing = items.stream()
                .filter(i -> variantId.equals(i.getVariantId()))
                .findFirst();

        if (quantity <= 0) {
            existing.ifPresent(cartItemRepository::delete);
            touchCart(cart);
            return new CartView();
        }

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        } else {
            CartItem item = new CartItem();
            item.setCartItemId(UUID.randomUUID());
            item.setCartId(cart.getCartId());
            item.setVariantId(variantId);
            item.setQuantity(quantity);
            item.setAddedAt(LocalDateTime.now());
            cartItemRepository.save(item);
        }

        touchCart(cart);
        return new CartView();
    }

    @Override
    public CartView removeItem(UUID userId, UUID cartItemId) {
        Cart cart = getOrCreateCart(userId);

        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        if (!cart.getCartId().equals(item.getCartId())) {
            throw new IllegalArgumentException("Cart item does not belong to this user cart");
        }

        cartItemRepository.delete(item);
        touchCart(cart);
        return new CartView();
    }

    @Override
    public CartView removeItemByVariant(UUID userId, UUID variantId) {
        Cart cart = getOrCreateCart(userId);

        List<CartItem> items = cartItemRepository.findByCartId(cart.getCartId());
        items.stream()
                .filter(i -> variantId.equals(i.getVariantId()))
                .findFirst()
                .ifPresent(cartItemRepository::delete);

        touchCart(cart);
        return new CartView();
    }

    @Override
    public CartView replaceAllItems(UUID userId, List<SetCartItemRequest> items) {
        Cart cart = getOrCreateCart(userId);

        List<CartItem> existing = cartItemRepository.findByCartId(cart.getCartId());
        if (!existing.isEmpty()) {
            cartItemRepository.deleteAll(existing);
        }

        if (items != null) {
            for (SetCartItemRequest req : items) {
                if (req == null) continue;
                if (req.quantity <= 0) continue;

                CartItem item = new CartItem();
                item.setCartItemId(UUID.randomUUID());
                item.setCartId(cart.getCartId());
                item.setVariantId(req.variantId);
                item.setQuantity(req.quantity);
                item.setAddedAt(LocalDateTime.now());
                cartItemRepository.save(item);
            }
        }

        touchCart(cart);
        return new CartView();
    }

    @Override
    public List<CartItemView> listItems(UUID userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItem> items = cartItemRepository.findByCartId(cart.getCartId());

        // If your CartItemView has fields, map them here.
        // For now, return same count with empty DTO objects.
        List<CartItemView> views = new ArrayList<>();
        for (CartItem ignored : items) {
            views.add(new CartItemView());
        }
        return views;
    }

    @Override
    public CartValidationResult validateCart(UUID userId) {
        return null;
    }

    @Override
    public CartValidationResult validateCartStock(UUID userId) {
        return null;
    }

    @Override
    public CartValidationResult validateCartStock(UUID userId, UUID warehouseId) {
        return null;
    }

    @Override
    public CartTotals getTotals(UUID userId) {
        return null;
    }

    @Override
    public CartView refreshCart(UUID userId) {
        return null;
    }

    @Override
    public void clearCart(UUID userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItem> items = cartItemRepository.findByCartId(cart.getCartId());
        if (!items.isEmpty()) {
            cartItemRepository.deleteAll(items);
        }
        touchCart(cart);
    }

    // -------------------------
    // Helpers
    // -------------------------

    private Cart getOrCreateCart(UUID userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setCartId(UUID.randomUUID());
                    cart.setUserId(userId);
                    cart.setCreatedAt(LocalDateTime.now());
                    cart.setUpdatedAt(LocalDateTime.now());
                    return cartRepository.save(cart);
                });
    }

    private void touchCart(Cart cart) {
        cart.setUpdatedAt(LocalDateTime.now());
        cartRepository.save(cart);
    }
}