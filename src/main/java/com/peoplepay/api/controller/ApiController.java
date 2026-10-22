package com.peoplepay.api.controller;

import com.peoplepay.api.dto.*;
import com.peoplepay.api.entity.*;
import com.peoplepay.api.service.PeoplePayService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {
	private final PeoplePayService service;
	private final AuthenticationManager authenticationManager;

	public ApiController(PeoplePayService service, AuthenticationManager authenticationManager) {
		this.service = service;
		this.authenticationManager = authenticationManager;
	}

	@PostMapping("/auth/login")
	public Map<String, Object> login(@Valid @RequestBody LoginRequest req) {
		Authentication a = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(req.username(), req.password()));
		String role = a.getAuthorities().stream().map(x -> x.getAuthority()).filter(x -> x.startsWith("ROLE_"))
				.map(x -> x.substring(5)).findFirst().orElse("HR");
		return Map.of("username", a.getName(), "role", role, "displayName",
				role.equals("ADMIN") ? "System Admin" : "HR Manager");
	}

	@GetMapping("/employees")
	public List<Employee> employees() {
		return service.employees();
	}

	@GetMapping("/employees/{id}")
	public Employee employee(@PathVariable String id) {
		return service.employee(id);
	}

	@PostMapping("/employees")
	@ResponseStatus(HttpStatus.CREATED)
	public Employee create(@Valid @RequestBody EmployeeRequest r, Authentication a) {
		return service.create(r, a.getName());
	}

	@PutMapping("/employees/{id}")
	public Employee update(@PathVariable String id, @Valid @RequestBody EmployeeRequest r, Authentication a) {
		return service.update(id, r, a.getName());
	}

	@PatchMapping("/employees/{id}/deactivate")
	public Employee deactivate(@PathVariable String id, Authentication a) {
		return service.deactivate(id, a.getName());
	}

	@PostMapping("/employees/import")
	public Map<String, Object> importEmployees(@Valid @RequestBody List<@Valid EmployeeRequest> rows,
			Authentication a) {
		return service.importEmployees(rows, a.getName());
	}

	@GetMapping("/audit")
	public List<Map<String, Object>> audit() {
		return service.audits().stream().map(x -> Map.<String, Object>of("at", x.getEventAt(), "user", x.getUsername(),
				"action", x.getAction(), "detail", x.getDetail())).toList();
	}

	@PostMapping("/audit")
	public Map<String, Object> createAudit(@RequestBody Map<String, String> r, Authentication a) {
		service.audit(a.getName(), r.getOrDefault("action", "APP_EVENT"), r.getOrDefault("detail", ""));
		return Map.of("ok", true);
	}

	@GetMapping("/countries")
	public List<Map<String, Object>> countries() {
		return service.countries().stream()
				.map(c -> Map.<String, Object>of("name", c.getName(), "code", c.getCode(), "currency", c.getCurrency(),
						"symbol", c.getSymbol(), "rate", c.getRateToInr(), "updated", c.getUpdated()))
				.toList();
	}

	@PutMapping("/countries/{code}")
	public Map<String, Object> updateCountry(@PathVariable String code, @RequestBody Map<String, BigDecimal> body,
			Authentication a) {
		Country c = service.updateCountry(code, body.get("rate"), a.getName());
		return Map.of("name", c.getName(), "code", c.getCode(), "currency", c.getCurrency(), "symbol", c.getSymbol(),
				"rate", c.getRateToInr(), "updated", c.getUpdated());
	}

	@GetMapping("/salary-revisions")
	public List<Map<String, Object>> revisions() {
		return service.revisions().stream()
				.map(r -> Map.<String, Object>of("id", r.getId(), "employeeId", r.getEmployeeId(), "oldSalary",
						r.getOldSalary(), "newSalary", r.getNewSalary(), "currency", r.getCurrency(), "effectiveDate",
						r.getEffectiveDate(), "reason", r.getReason(), "user", r.getUsername(), "at", r.getEventAt()))
				.toList();
	}

	@PostMapping("/salary-revisions")
	public Map<String, Object> revise(@Valid @RequestBody RevisionRequest r, Authentication a) {
		SalaryRevision x = service.revise(r, a.getName());
		return Map.of("id", x.getId(), "employeeId", x.getEmployeeId(), "oldSalary", x.getOldSalary(), "newSalary",
				x.getNewSalary(), "currency", x.getCurrency(), "effectiveDate", x.getEffectiveDate(), "reason",
				x.getReason(), "user", x.getUsername(), "at", x.getEventAt());
	}
}
