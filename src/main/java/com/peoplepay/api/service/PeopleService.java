package com.peoplepay.api.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.peoplepay.api.dto.EmployeeRequest;
import com.peoplepay.api.dto.RevisionRequest;
import com.peoplepay.api.entity.AuditEvent;
import com.peoplepay.api.entity.Country;
import com.peoplepay.api.entity.Employee;
import com.peoplepay.api.entity.SalaryRevision;
import com.peoplepay.api.repository.AuditEventRepository;
import com.peoplepay.api.repository.CountryRepository;
import com.peoplepay.api.repository.EmployeeRepository;
import com.peoplepay.api.repository.SalaryRevisionRepository;

@Service
@Transactional
public class PeopleService {
	
	private final EmployeeRepository employees;
	private final AuditEventRepository audits;
	private final CountryRepository countries;
	private final SalaryRevisionRepository revisions;

	public PeopleService(EmployeeRepository employees, AuditEventRepository audits, CountryRepository countries,
			SalaryRevisionRepository revisions) {
		this.employees = employees;
		this.audits = audits;
		this.countries = countries;
		this.revisions = revisions;
	}

	@Transactional(readOnly = true)
	public List<Employee> employees() {
		return employees.findAllByOrderByNameAsc();
	}

	@Transactional(readOnly = true)
	public Employee employee(String id) {
		return employees.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found."));
	}

	public Employee create(EmployeeRequest r, String user) {
		if (employees.existsById(r.id()) || employees.existsByEmailIgnoreCase(r.email()))
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Employee ID or email already exists.");
		Employee e = map(r);
		employees.save(e);
		audit(user, "EMPLOYEE_CREATED", e.getId() + " · " + e.getName());
		return e;
	}

	public Employee update(String id, EmployeeRequest r, String user) {
		if (!id.equals(r.id()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Path ID and employee ID must match.");
		Employee e = employee(id);
		if (!e.getEmail().equalsIgnoreCase(r.email()) && employees.existsByEmailIgnoreCase(r.email()))
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists.");
		e.updateFrom(map(r));
		audit(user, "EMPLOYEE_UPDATED", id + " · " + e.getName());
		return e;
	}

	public Employee deactivate(String id, String user) {
		Employee e = employee(id);
		e.setStatus("Inactive");
		audit(user, "EMPLOYEE_DEACTIVATED", id);
		return e;
	}

	public Map<String, Object> importEmployees(List<EmployeeRequest> rows, String user) {
		Set<String> ids = new HashSet<>(), emails = new HashSet<>();
		for (EmployeeRequest r : rows) {
			if (!ids.add(r.id()) || !emails.add(r.email().toLowerCase(Locale.ROOT)) || employees.existsById(r.id())
					|| employees.existsByEmailIgnoreCase(r.email()))
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicate ID or email in import: " + r.id());
		}
		employees.saveAll(rows.stream().map(this::map).toList());
		audit(user, "EMPLOYEE_IMPORT", rows.size() + " employees imported");
		return Map.of("imported", rows.size());
	}

	@Transactional(readOnly = true)
	public List<AuditEvent> audits() {
		return audits.findAllByOrderByEventAtDescIdDesc(PageRequest.of(0, 500));
	}

	public void audit(String user, String action, String detail) {
		audits.save(new AuditEvent(user, action, detail));
	}

	@Transactional(readOnly = true)
	public List<Country> countries() {
		return countries.findAll(org.springframework.data.domain.Sort.by("name").ascending());
	}

	public Country updateCountry(String code, BigDecimal rate, String user) {
		if (rate == null || rate.signum() < 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rate must be non-negative.");
		Country c = countries.findById(code)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Country not found."));
		c.updateRate(rate);
		audit(user, "COUNTRY_RATE_UPDATED", code + " rate " + rate);
		return c;
	}

	@Transactional(readOnly = true)
	public List<SalaryRevision> revisions() {
		return revisions.findAllByOrderByEventAtDesc();
	}

	public SalaryRevision revise(RevisionRequest r, String user) {
		Employee e = employee(r.employeeId());
		SalaryRevision rev = new SalaryRevision(UUID.randomUUID().toString(), e.getId(), e.getSalary(), r.newSalary(),
				e.getCurrency(), r.effectiveDate(), r.reason(), user);
		revisions.save(rev);
		e.setSalary(r.newSalary());
		audit(user, "SALARY_REVISED", e.getId() + ": " + rev.getOldSalary() + " → " + rev.getNewSalary() + " "
				+ e.getCurrency() + "; " + r.reason());
		return rev;
	}

	private Employee map(EmployeeRequest r) {
		return new Employee(r.id(), r.name(), r.email(), r.department(), r.designation(), r.country(),
				r.currency().toUpperCase(Locale.ROOT), r.salary(), r.status(), r.joined());
	}
}
