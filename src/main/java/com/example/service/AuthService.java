package com.example.service;

import com.example.dto.LoginRequest;
import com.example.dto.RegisterRequest;
import com.example.entity.Role;
import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
			AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	// ================= REGISTER =================
	public String register(RegisterRequest request) {

		// Check if email already exists
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new RuntimeException("User already exists");
		}

		// Create new user
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(Role.USER);

		userRepository.save(user);

		// Generate JWT token
		return jwtService.generateToken(user.getEmail());
	}

	// ================= LOGIN =================
	public String login(LoginRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		return jwtService.generateToken(request.getEmail());
	}
}