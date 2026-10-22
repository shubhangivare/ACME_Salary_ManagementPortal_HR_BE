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

import com.peoplepay.api.service.AuditService;


@RestController
@RequestMapping("/api/audit")
public class AuditController {
	private final AuditService auditService;

	public AuditController(AuditService auditService) {
		this.auditService = auditService;
	}

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> getAuditEvents() {
		return ResponseEntity
				.ok(auditService
						.audits().stream().map(event -> Map.<String, Object>of("at", event.getEventAt(), "user",
								event.getUsername(), "action", event.getAction(), "detail", event.getDetail()))
						.toList());
	}

	@PostMapping
	public ResponseEntity<Map<String, Object>> createAuditEvent(@RequestBody Map<String, String> request,
			Authentication authentication) {
		auditService.audit(authentication.getName(), request.getOrDefault("action", "APP_EVENT"),
				request.getOrDefault("detail", ""));
		return ResponseEntity.ok(Map.of("ok", true));
	}
}
