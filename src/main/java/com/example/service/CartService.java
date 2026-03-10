
package com.example.service;

import com.example.entity.*;
import com.example.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;

	public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
			UserRepository userRepository, ProductRepository productRepository) {

		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}

	// ================= ADD TO CART =================
	@Transactional
	public Cart addToCart(String userEmail, Long productId, int quantity) {

		User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new RuntimeException("Product not found"));

		if (product.getQuantity() < quantity) {
			throw new RuntimeException("Not enough stock available");
		}

		Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUser(user);
			newCart.setItems(new ArrayList<>());
			return cartRepository.save(newCart);
		});

		CartItem existingItem = cart.getItems().stream().filter(item -> item.getProduct().getId().equals(productId))
				.findFirst().orElse(null);

		if (existingItem != null) {

			// Increase quantity in cart
			existingItem.setQuantity(existingItem.getQuantity() + quantity);
			cartItemRepository.save(existingItem);

		} else {

			CartItem cartItem = new CartItem();
			cartItem.setCart(cart);
			cartItem.setProduct(product);
			cartItem.setQuantity(quantity);

			cart.getItems().add(cartItem);
			cartItemRepository.save(cartItem);
		}

		// 🔥 Reduce stock AFTER cart update
		product.setQuantity(product.getQuantity() - quantity);
		productRepository.save(product);

		return cartRepository.save(cart);
	}

	// ================= GET USER CART =================
	@Transactional
	public Cart getUserCart(String userEmail) {

		User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

		Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUser(user);
			newCart.setItems(new ArrayList<>());
			return cartRepository.save(newCart);
		});

		cart.getItems().size();
		return cart;
	}

	// ================= UPDATE QUANTITY =================
	@Transactional
	public Cart updateQuantity(String email, Long productId, int change) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

		for (CartItem item : cart.getItems()) {

			if (item.getProduct().getId().equals(productId)) {

				Product product = item.getProduct();

				if (change > 0) {

					if (product.getQuantity() < 1) {
						throw new RuntimeException("Stock not available");
					}

					item.setQuantity(item.getQuantity() + 1);
					product.setQuantity(product.getQuantity() - 1);

				} else {

					item.setQuantity(item.getQuantity() - 1);
					product.setQuantity(product.getQuantity() + 1);

					if (item.getQuantity() <= 0) {
						cartItemRepository.delete(item);
						cart.getItems().remove(item);
						productRepository.save(product);
						break;
					}
				}

				cartItemRepository.save(item);
				productRepository.save(product);
				break;
			}
		}

		return cartRepository.save(cart);
	}

	// ================= REMOVE ITEM =================
	@Transactional
	public Cart removeItem(String email, Long productId) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

		cart.getItems().removeIf(item -> {

			if (item.getProduct().getId().equals(productId)) {

				Product product = item.getProduct();

				// Restore stock
				product.setQuantity(product.getQuantity() + item.getQuantity());
				productRepository.save(product);

				cartItemRepository.delete(item);

				return true;
			}

			return false;
		});

		return cartRepository.save(cart);
	}
}
