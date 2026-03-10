
package com.example.controller;

import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin("*")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	// ================= REGISTER =================
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

		try {

			String token = authService.register(request);

			return ResponseEntity.ok(Map.of("token", token));

		} catch (RuntimeException e) {

			// Important: send message for frontend
			return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
		}
	}

	// ================= LOGIN =================
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest request) {

		try {

			String token = authService.login(request);

			return ResponseEntity.ok(Map.of("token", token));

		} catch (Exception e) {

			return ResponseEntity.status(401).body(Map.of("message", "Invalid email or password"));
		}
	}
}
