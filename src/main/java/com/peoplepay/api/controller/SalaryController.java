package com.peoplepay.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peoplepay.api.dto.RevisionRequest;
import com.peoplepay.api.entity.SalaryRevision;
import com.peoplepay.api.service.SalaryService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/salary-revisions")
public class SalaryController {
	private final SalaryService salaryService;

	public SalaryController(SalaryService salaryService) {
		this.salaryService = salaryService;
	}

	private Map<String, Object> toMap(SalaryRevision revision) {
		return Map.of("id", revision.getId(), "employeeId", revision.getEmployeeId(), "oldSalary",
				revision.getOldSalary(), "newSalary", revision.getNewSalary(), "currency", revision.getCurrency(),
				"effectiveDate", revision.getEffectiveDate(), "reason", revision.getReason(), "user",
				revision.getUsername(), "at", revision.getEventAt());
	}

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> getSalaryRevisions() {
		return ResponseEntity.ok(salaryService.revisions().stream().map(this::toMap).toList());
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> createSalaryRevision(@Valid @RequestBody RevisionRequest request,
			Authentication authentication) {
		return ResponseEntity.status(201).body(toMap(salaryService.revise(request, authentication.getName())));
	}
}
