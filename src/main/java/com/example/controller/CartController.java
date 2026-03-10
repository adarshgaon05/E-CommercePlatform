package com.example.controller;

import com.example.entity.Cart;
import com.example.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // ================= ADD TO CART =================
    @PostMapping("/add/{productId}")
    public Cart addToCart(@PathVariable Long productId,
                          @RequestParam(defaultValue = "1") int quantity,
                          Authentication authentication) {

        String userEmail = authentication.getName();
        return cartService.addToCart(userEmail, productId, quantity);
    }

    // ================= GET CART =================
    @GetMapping
    public Cart getCart(Authentication authentication) {

        String userEmail = authentication.getName();
        return cartService.getUserCart(userEmail);
    }

    // ================= INCREASE QUANTITY =================
    @PostMapping("/increase/{productId}")
    public Cart increaseQuantity(@PathVariable Long productId,
                                 Authentication authentication) {

        String userEmail = authentication.getName();
        return cartService.updateQuantity(userEmail, productId, 1);
    }

    // ================= DECREASE QUANTITY =================
    @PostMapping("/decrease/{productId}")
    public Cart decreaseQuantity(@PathVariable Long productId,
                                 Authentication authentication) {

        String userEmail = authentication.getName();
        return cartService.updateQuantity(userEmail, productId, -1);
    }

    // ================= REMOVE ITEM =================
    @DeleteMapping("/remove/{productId}")
    public Cart removeItem(@PathVariable Long productId,
                           Authentication authentication) {

        String userEmail = authentication.getName();
        return cartService.removeItem(userEmail, productId);
    }
}