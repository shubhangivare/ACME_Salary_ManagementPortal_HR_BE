package com.peoplepay.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.peoplepay.api.entity.Employee;

import org.springframework.data.domain.Sort;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
	boolean existsByEmailIgnoreCase(String email);

	java.util.List<Employee> findAllByOrderByNameAsc();
}
