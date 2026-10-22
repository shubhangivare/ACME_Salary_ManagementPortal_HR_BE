package com.peoplepay.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peoplepay.api.entity.SalaryRevision;

public interface SalaryRevisionRepository extends JpaRepository<SalaryRevision, String> {
	List<SalaryRevision> findAllByOrderByEventAtDesc();
}
