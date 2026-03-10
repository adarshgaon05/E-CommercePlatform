package com.example.service;

import com.example.entity.*;
import com.example.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final CartRepository cartRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;

	public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
			CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {

		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.cartRepository = cartRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
	}

	// ================= CHECKOUT =================

	@Transactional
	public Order checkout(String userEmail) {

		User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));

		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Cart not found"));

		if (cart.getItems() == null || cart.getItems().isEmpty()) {
			throw new RuntimeException("Cart is empty");
		}

		double totalAmount = 0;

		Order order = new Order();
		order.setUser(user);
		order.setStatus("PLACED");
		order.setCreatedAt(LocalDateTime.now());

		order = orderRepository.save(order);

		List<OrderItem> orderItems = new ArrayList<>();

		for (CartItem cartItem : cart.getItems()) {

			Product product = cartItem.getProduct();

			if (product.getQuantity() < cartItem.getQuantity()) {
				throw new RuntimeException("Not enough stock for " + product.getName());
			}

			// reduce stock
			product.setQuantity(product.getQuantity() - cartItem.getQuantity());
			productRepository.save(product);

			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(product);
			orderItem.setQuantity(cartItem.getQuantity());

			orderItemRepository.save(orderItem);

			orderItems.add(orderItem);

			totalAmount += product.getPrice() * cartItem.getQuantity();
		}

		order.setItems(orderItems);
		order.setTotalAmount(totalAmount);

		orderRepository.save(order);

		// clear cart
		cart.getItems().clear();
		cartRepository.save(cart);

		return order;
	}

	// ================= GET USER ORDERS =================

	public List<Order> getUserOrders(String email) {

		User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

		return orderRepository.findByUser(user);
	}
}