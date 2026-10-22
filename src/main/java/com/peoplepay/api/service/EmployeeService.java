package com.peoplepay.api.service;

import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.peoplepay.api.dto.EmployeeRequest;
import com.peoplepay.api.entity.Employee;

public interface EmployeeService {
	List<Employee> employees();
	Page<Employee> searchPage(String search, String department, String country, String status, Pageable pageable);

	Employee employee(String id);

	Employee create(EmployeeRequest request, String username);

	Employee update(String id, EmployeeRequest request, String username);

	Employee deactivate(String id, String username);

	Map<String, Object> importEmployees(List<EmployeeRequest> rows, String username);
}
