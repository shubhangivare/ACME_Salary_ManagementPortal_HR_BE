package com.peoplepay.api.controller;

import java.util.List;
import java.util.Map;

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
