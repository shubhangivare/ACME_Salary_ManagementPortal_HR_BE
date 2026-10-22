package com.peoplepay.api.controller;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peoplepay.api.dto.EmployeeRequest;
import com.peoplepay.api.entity.Employee;
import com.peoplepay.api.service.EmployeeService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
	private final EmployeeService employeeService;

	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	@GetMapping
	public ResponseEntity<List<Employee>> getEmployees() {
		return ResponseEntity.ok(employeeService.employees());
	}

	@GetMapping("/page")
	public ResponseEntity<Map<String, Object>> getEmployeesPage(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "15") int size,
			@RequestParam(required = false) String search,
			@RequestParam(required = false) String department,
			@RequestParam(required = false) String country,
			@RequestParam(required = false) String status) {
		int safePage = Math.max(0, page);
		int safeSize = Math.max(1, Math.min(size, 100));
		Page<Employee> result = employeeService.searchPage(blankToNull(search), blankToNull(department),
				blankToNull(country), blankToNull(status),
				PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.ASC, "name")));
		Map<String, Object> body = new HashMap<>();
		body.put("content", result.getContent());
		body.put("page", result.getNumber());
		body.put("size", result.getSize());
		body.put("totalElements", result.getTotalElements());
		body.put("totalPages", result.getTotalPages());
		body.put("first", result.isFirst());
		body.put("last", result.isLast());
		return ResponseEntity.ok(body);
	}

	private String blankToNull(String value) {
		return value == null || value.isBlank() ? null : value.trim();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Employee> getEmployee(@PathVariable String id) {
		return ResponseEntity.ok(employeeService.employee(id));
	}

	@PostMapping
	public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeRequest request,
			Authentication authentication) {
		return ResponseEntity.status(201).body(employeeService.create(request, authentication.getName()));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable String id, @Valid @RequestBody EmployeeRequest request,
			Authentication authentication) {
		return ResponseEntity.ok(employeeService.update(id, request, authentication.getName()));
	}

	@PatchMapping("/{id}/deactivate")
	public ResponseEntity<Employee> deactivateEmployee(@PathVariable String id, Authentication authentication) {
		return ResponseEntity.ok(employeeService.deactivate(id, authentication.getName()));
	}

	@PostMapping("/import")
	public ResponseEntity<Map<String, Object>> importEmployees(@Valid @RequestBody List<@Valid EmployeeRequest> rows,
			Authentication authentication) {
		return ResponseEntity.ok(employeeService.importEmployees(rows, authentication.getName()));
	}
}
