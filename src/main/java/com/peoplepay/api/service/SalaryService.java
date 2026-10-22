package com.peoplepay.api.service;

import java.util.List;

import com.peoplepay.api.dto.RevisionRequest;
import com.peoplepay.api.entity.SalaryRevision;

public interface SalaryService {
	List<SalaryRevision> revisions();

	SalaryRevision revise(RevisionRequest request, String username);
}
