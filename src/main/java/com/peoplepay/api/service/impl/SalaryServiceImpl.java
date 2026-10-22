package com.peoplepay.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.peoplepay.api.dto.RevisionRequest;
import com.peoplepay.api.entity.SalaryRevision;
import com.peoplepay.api.service.PeopleService;
import com.peoplepay.api.service.SalaryService;

@Service
public class SalaryServiceImpl implements SalaryService {
	private final PeopleService peopleService;

	public SalaryServiceImpl(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	@Override
	public List<SalaryRevision> revisions() {
		return peopleService.revisions();
	}

	@Override
	public SalaryRevision revise(RevisionRequest request, String username) {
		return peopleService.revise(request, username);
	}
}
