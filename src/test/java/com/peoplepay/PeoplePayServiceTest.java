package com.peoplepay;

import com.peoplepay.api.dto.EmployeeRequest;
import com.peoplepay.api.entity.Employee;
import com.peoplepay.api.repository.*;
import com.peoplepay.api.service.PeoplePayService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PeoplePayServiceTest {
	private EmployeeRepository employees;
	private AuditEventRepository audits;
	private CountryRepository countries;
	private SalaryRevisionRepository revisions;
	private PeoplePayService service;

	@BeforeEach
	void setUp() {
		employees = mock(EmployeeRepository.class);
		audits = mock(AuditEventRepository.class);
		countries = mock(CountryRepository.class);
		revisions = mock(SalaryRevisionRepository.class);
		service = new PeoplePayService(employees, audits, countries, revisions);
	}

	private EmployeeRequest request(String id, String email) {
		return new EmployeeRequest(id, "Test User", email, "Engineering", "Developer", "India", "inr",
				new BigDecimal("100000"), "Active", LocalDate.of(2024, 1, 1));
	}

	@Test
	void createEmployeeNormalizesCurrencyAndAudits() {
		when(employees.existsById("E1")).thenReturn(false);
		when(employees.existsByEmailIgnoreCase(anyString())).thenReturn(false);
		when(employees.save(any(Employee.class))).thenAnswer(i -> i.getArgument(0));
		Employee e = service.create(request("E1", "test@example.com"), "hr");
		assertEquals("INR", e.getCurrency());
		assertEquals("E1", e.getId());
		verify(audits).save(any());
	}

	@Test
	void createRejectsDuplicateId() {
		when(employees.existsById("E1")).thenReturn(true);
		assertThrows(ResponseStatusException.class, () -> service.create(request("E1", "test@example.com"), "hr"));
		verify(employees, never()).save(any());
	}

	@Test
	void getMissingEmployeeReturnsNotFound() {
		when(employees.findById("missing")).thenReturn(Optional.empty());
		ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> service.employee("missing"));
		assertEquals(404, ex.getStatusCode().value());
	}

	@Test
	void updateRejectsMismatchedPathId() {
		assertThrows(ResponseStatusException.class,
				() -> service.update("E2", request("E1", "test@example.com"), "hr"));
	}

	@Test
	void deactivateSetsInactive() {
		Employee e = new Employee("E1", "Test User", "test@example.com", "Engineering", "Developer", "India", "INR",
				BigDecimal.ONE, "Active", LocalDate.now());
		when(employees.findById("E1")).thenReturn(Optional.of(e));
		assertEquals("Inactive", service.deactivate("E1", "hr").getStatus());
		verify(audits).save(any());
	}

	@Test
	void importRejectsDuplicatesWithinBatch() {
		assertThrows(ResponseStatusException.class, () -> service
				.importEmployees(java.util.List.of(request("E1", "x@a.com"), request("E1", "y@a.com")), "hr"));
		verify(employees, never()).saveAll(anyList());
	}
}
