package com.peoplepay.api.service;

import java.util.List;
import java.util.Map;

public interface AuthService {
	Map<String, Object> login(String username, String password);

	Map<String, Object> register(String username, String password, String displayName, String role);

	List<String> availableRoles();
}
