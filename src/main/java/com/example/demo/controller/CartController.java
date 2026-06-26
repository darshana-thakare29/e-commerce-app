package com.example.demo.controller;
import com.example.demo.dto.AddCartItemRequest;
import com.example.demo.dto.AddProductToCartRequest;
import com.example.demo.dto.CartItemView;
import com.example.demo.dto.CartView;
import com.example.demo.dto.SetCartItemRequest;
import com.example.demo.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartView> getCart(@RequestParam UUID userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItemView>> listItems(@RequestParam UUID userId) {
        return ResponseEntity.ok(cartService.listItems(userId));
    }

    @PostMapping("/items")
    public ResponseEntity<CartView> addItem(@RequestParam UUID userId, @RequestBody AddCartItemRequest request) {
        return ResponseEntity.ok(cartService.addItem(userId, request.variantId, request.quantity));
    }

    @PostMapping("/products")
    public ResponseEntity<CartView> addProduct(@RequestParam UUID userId, @RequestBody AddProductToCartRequest request) {
        return ResponseEntity.ok(cartService.addProduct(userId, request.productId, request.quantity));
    }

    @PostMapping("/items/bulk")
    public ResponseEntity<CartView> addItems(@RequestParam UUID userId, @RequestBody List<AddCartItemRequest> items) {
        return ResponseEntity.ok(cartService.addItems(userId, items));
    }

    @PutMapping("/items/{cartItemId}/quantity")
    public ResponseEntity<CartView> updateItemQuantity(@RequestParam UUID userId,
                                                       @PathVariable UUID cartItemId,
                                                       @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.updateItemQuantity(userId, cartItemId, quantity));
    }

    @PutMapping("/items/variant/{variantId}/quantity")
    public ResponseEntity<CartView> setItemQuantity(@RequestParam UUID userId,
                                                    @PathVariable UUID variantId,
                                                    @RequestParam int quantity) {
        return ResponseEntity.ok(cartService.setItemQuantity(userId, variantId, quantity));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartView> removeItem(@RequestParam UUID userId, @PathVariable UUID cartItemId) {
        return ResponseEntity.ok(cartService.removeItem(userId, cartItemId));
    }

    @DeleteMapping("/items/variant/{variantId}")
    public ResponseEntity<CartView> removeItemByVariant(@RequestParam UUID userId, @PathVariable UUID variantId) {
        return ResponseEntity.ok(cartService.removeItemByVariant(userId, variantId));
    }

    @PutMapping("/items/replace")
    public ResponseEntity<CartView> replaceAllItems(@RequestParam UUID userId, @RequestBody List<SetCartItemRequest> items) {
        return ResponseEntity.ok(cartService.replaceAllItems(userId, items));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart(@RequestParam UUID userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}
