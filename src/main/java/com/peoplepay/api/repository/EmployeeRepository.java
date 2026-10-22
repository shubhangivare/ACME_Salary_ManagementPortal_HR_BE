package com.peoplepay.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peoplepay.api.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
	boolean existsByEmailIgnoreCase(String email);

	java.util.List<Employee> findAllByOrderByNameAsc();

}
