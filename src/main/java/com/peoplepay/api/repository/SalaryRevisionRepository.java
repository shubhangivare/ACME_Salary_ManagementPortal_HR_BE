package com.peoplepay.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.peoplepay.api.entity.SalaryRevision;

import java.util.List;

@Repository
public interface SalaryRevisionRepository extends JpaRepository<SalaryRevision, String> {
	List<SalaryRevision> findAllByOrderByEventAtDesc();
}
