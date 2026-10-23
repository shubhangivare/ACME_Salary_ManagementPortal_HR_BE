package com.peoplepay.api.controller;

import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.peoplepay.api.dto.LoginRequest;
import com.peoplepay.api.dto.SignupRequest;
import com.peoplepay.api.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/login")
	public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.login(request.username(), request.password()));
	}

	@PostMapping("/register")
	public ResponseEntity<Map<String, Object>> register(@Valid @RequestBody SignupRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(
				authService.register(request.username(), request.password(), request.displayName(), request.role()));
	}

	@GetMapping("/roles")
	public List<String> roles() {
		return authService.availableRoles();
	}
}
