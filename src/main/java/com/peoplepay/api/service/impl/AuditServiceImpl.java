package com.peoplepay.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.peoplepay.api.entity.AuditEvent;
import com.peoplepay.api.service.AuditService;
import com.peoplepay.api.service.PeopleService;

@Service
public class AuditServiceImpl implements AuditService {
	private final PeopleService peopleService;

	public AuditServiceImpl(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	@Override
	public List<AuditEvent> audits() {
		return peopleService.audits();
	}

	@Override
	public void audit(String username, String action, String detail) {
		peopleService.audit(username, action, detail);
	}
}
