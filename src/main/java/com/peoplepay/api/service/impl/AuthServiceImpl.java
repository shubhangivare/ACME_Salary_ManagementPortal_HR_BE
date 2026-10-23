package com.peoplepay.api.service.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import com.peoplepay.api.entity.AppRole;
import com.peoplepay.api.entity.AppUser;
import com.peoplepay.api.repository.AppRoleRepository;
import com.peoplepay.api.repository.AppUserRepository;
import com.peoplepay.api.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService {
	private final AuthenticationManager authenticationManager;
	private final AppUserRepository users;
	private final AppRoleRepository roles;
	private final PasswordEncoder encoder;

	public AuthServiceImpl(AuthenticationManager manager, AppUserRepository users, AppRoleRepository roles,
			PasswordEncoder encoder) {
		this.authenticationManager = manager;
		this.users = users;
		this.roles = roles;
		this.encoder = encoder;
	}

	@Override
	public Map<String, Object> login(String username, String password) {
		Authentication a = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username.trim(), password));
		AppUser u = users.findByUsernameWithRole(a.getName())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
		return Map.of("username", u.getUsername(), "role", u.getRole().getName(), "displayName", u.getDisplayName());
	}

	@Override
	@Transactional
	public Map<String, Object> register(String username, String password, String displayName, String roleName) {
		String normalized = username.trim();
		if (!normalized.matches("[A-Za-z0-9._-]{3,50}"))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Username must be 3–50 letters, numbers, dots, underscores or hyphens");
		if (users.existsByUsernameIgnoreCase(normalized))
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Username is already registered");
		if (password.length() < 8 || password.length() > 72)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be 8–72 characters");
		if (!"HR".equalsIgnoreCase(roleName))
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Public registration is limited to the HR role");
		AppRole role = roles.findByName("HR").orElseThrow(() -> new IllegalStateException("HR role is not configured"));
		AppUser user = users.saveAndFlush(new AppUser(normalized, encoder.encode(password), displayName.trim(), role));
		return Map.of("username", user.getUsername(), "role", user.getRole().getName(), "displayName",
				user.getDisplayName());
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> availableRoles() {
		List<String> availableRoleList = roles.findAll()
		        .stream()
		        .map(AppRole::getName)
		        .collect(Collectors.toList());
		return availableRoleList;
	}
}
