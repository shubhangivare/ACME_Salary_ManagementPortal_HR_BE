package com.peoplepay.api.service.impl;

import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import com.peoplepay.api.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
	private final AuthenticationManager authenticationManager;

	public AuthServiceImpl(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Map<String, Object> login(String username, String password) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		String role = authentication.getAuthorities().stream().map(a -> a.getAuthority())
				.filter(a -> a.startsWith("ROLE_")).map(a -> a.substring(5)).findFirst().orElse("HR");
		return Map.of("username", authentication.getName(), "role", role, "displayName",
				role.equals("ADMIN") ? "System Admin" : "HR Manager");
	}
}
