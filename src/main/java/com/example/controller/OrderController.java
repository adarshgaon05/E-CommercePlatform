package com.example.controller;

import com.example.entity.Order;
import com.example.service.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	// ================= CHECKOUT =================

	@PostMapping("/checkout")
	public ResponseEntity<?> checkout(Authentication authentication) {

		if (authentication == null) {
			return ResponseEntity.status(401).body("User not logged in");
		}

		String email = authentication.getName();

		Order order = orderService.checkout(email);

		return ResponseEntity.ok(order);
	}

	// ================= GET USER ORDERS =================

	@GetMapping("/my-orders")
	public ResponseEntity<?> getMyOrders(Authentication authentication) {

		if (authentication == null) {
			return ResponseEntity.status(401).body("User not logged in");
		}

		String email = authentication.getName();

		List<Order> orders = orderService.getUserOrders(email);

		return ResponseEntity.ok(orders);
	}
}