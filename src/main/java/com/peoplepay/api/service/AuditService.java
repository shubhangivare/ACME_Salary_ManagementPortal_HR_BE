package com.peoplepay.api.service;

import java.util.List;

import com.peoplepay.api.entity.AuditEvent;

public interface AuditService {
	List<AuditEvent> audits();

	void audit(String username, String action, String detail);
}
