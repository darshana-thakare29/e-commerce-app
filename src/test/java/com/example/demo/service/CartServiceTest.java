package com.example.demo.service;

import com.example.demo.entity.Cart;
import com.example.demo.entity.CartItem;
import com.example.demo.repository.CartItemRepository;
import com.example.demo.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void testAddItems_nullList() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartService.addItems(userId, null);

        verify(cartRepository, atLeastOnce()).findByUserId(userId);
    }

    @Test
    void testAddItems_emptyList() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartService.addItems(userId, Collections.emptyList());

        verify(cartRepository, atLeastOnce()).findByUserId(userId);
    }

    @Test
    void testAddItems_validList() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        var request = new com.example.demo.dto.AddCartItemRequest();
        request.variantId = UUID.randomUUID();
        request.quantity = 2;

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId())).thenReturn(List.of());

        cartService.addItems(userId, List.of(request));

        verify(cartItemRepository, atLeastOnce()).save(any());
    }

    @Test
    void testSetItemQuantity_createNew() {
        UUID userId = UUID.randomUUID();
        UUID variantId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId())).thenReturn(List.of());

        cartService.setItemQuantity(userId, variantId, 5);

        verify(cartItemRepository).save(any());
    }

    @Test
    void testSetItemQuantity_updateExisting() {
        UUID userId = UUID.randomUUID();
        UUID variantId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setVariantId(variantId);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId())).thenReturn(List.of(item));

        cartService.setItemQuantity(userId, variantId, 3);

        verify(cartItemRepository).save(item);
    }

    @Test
    void testSetItemQuantity_delete() {
        UUID userId = UUID.randomUUID();
        UUID variantId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setVariantId(variantId);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId())).thenReturn(List.of(item));

        cartService.setItemQuantity(userId, variantId, 0);

        verify(cartItemRepository).delete(item);
    }

    @Test
    void testRemoveItemByVariant_matching() {
        UUID userId = UUID.randomUUID();
        UUID variantId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setVariantId(variantId);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId())).thenReturn(List.of(item));

        cartService.removeItemByVariant(userId, variantId);

        verify(cartItemRepository).delete(item);
    }

    @Test
    void testRemoveItemByVariant_noMatch() {
        UUID userId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId())).thenReturn(List.of());

        cartService.removeItemByVariant(userId, UUID.randomUUID());

        verify(cartItemRepository, never()).delete(any());
    }

    @Test
    void testUpdateItemQuantity_updateCase() {
        UUID userId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setCartItemId(itemId);
        item.setCartId(cart.getCartId());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(item));

        cartService.updateItemQuantity(userId, itemId, 5);

        verify(cartItemRepository).save(item);
    }

    @Test
    void testUpdateItemQuantity_wrongCart() {
        UUID userId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setCartItemId(itemId);
        item.setCartId(UUID.randomUUID());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findById(itemId)).thenReturn(Optional.of(item));

        assertThrows(IllegalArgumentException.class,
                () -> cartService.updateItemQuantity(userId, itemId, 5));
    }

    @Test
    void testAddItem_invalidQuantity() {
        UUID userId = UUID.randomUUID();
        UUID variantId = UUID.randomUUID();

        assertThrows(IllegalArgumentException.class,
                () -> cartService.addItem(userId, variantId, 0));
    }

    @Test
    void testAddItem_existingItem() {
        UUID userId = UUID.randomUUID();
        UUID variantId = UUID.randomUUID();

        Cart cart = new Cart();
        cart.setCartId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setVariantId(variantId);
        item.setQuantity(2);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartItemRepository.findByCartId(cart.getCartId())).thenReturn(List.of(item));

        cartService.addItem(userId, variantId, 3);

        verify(cartItemRepository).save(item);
    }
}