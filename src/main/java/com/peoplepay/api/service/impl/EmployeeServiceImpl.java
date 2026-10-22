package com.peoplepay.api.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.peoplepay.api.dto.EmployeeRequest;
import com.peoplepay.api.entity.Employee;
import com.peoplepay.api.service.EmployeeService;
import com.peoplepay.api.service.PeopleService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	private final PeopleService peopleService;

	public EmployeeServiceImpl(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	@Override
	public List<Employee> employees() {
		return peopleService.employees();
	}

	@Override
	public Employee employee(String id) {
		return peopleService.employee(id);
	}

	@Override
	public Employee create(EmployeeRequest request, String username) {
		return peopleService.create(request, username);
	}

	@Override
	public Employee update(String id, EmployeeRequest request, String username) {
		return peopleService.update(id, request, username);
	}

	@Override
	public Employee deactivate(String id, String username) {
		return peopleService.deactivate(id, username);
	}

	@Override
	public Map<String, Object> importEmployees(List<EmployeeRequest> rows, String username) {
		return peopleService.importEmployees(rows, username);
	}
}
