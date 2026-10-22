package com.peoplepay.api;

import com.peoplepay.api.dto.EmployeeRequest;
import com.peoplepay.api.entity.Employee;
import com.peoplepay.api.repository.AuditEventRepository;
import com.peoplepay.api.repository.CountryRepository;
import com.peoplepay.api.repository.EmployeeRepository;
import com.peoplepay.api.repository.SalaryRevisionRepository;
import com.peoplepay.api.service.PeopleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** Unit tests for business rules in PeopleService, with persistence mocked. */
class ServiceLogicTest {
    private EmployeeRepository employeeRepository;
    private AuditEventRepository auditEventRepository;
    private CountryRepository countryRepository;
    private SalaryRevisionRepository salaryRevisionRepository;
    private PeopleService peopleService;

    @BeforeEach
    void setUp() {
        employeeRepository = mock(EmployeeRepository.class);
        auditEventRepository = mock(AuditEventRepository.class);
        countryRepository = mock(CountryRepository.class);
        salaryRevisionRepository = mock(SalaryRevisionRepository.class);
        peopleService = new PeopleService(employeeRepository, auditEventRepository, countryRepository, salaryRevisionRepository);
    }

    @Test
    void createEmployeeUppercasesCurrencyAndWritesAudit() {
        when(employeeRepository.existsById("E-TEST")).thenReturn(false);
        when(employeeRepository.existsByEmailIgnoreCase("person@example.com")).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee employee = peopleService.create(request("E-TEST", "person@example.com", "inr"), "hr");

        assertEquals("INR", employee.getCurrency());
        assertEquals("E-TEST", employee.getId());
        verify(employeeRepository).save(any(Employee.class));
        verify(auditEventRepository).save(argThat(event -> "EMPLOYEE_CREATED".equals(event.getAction())));
    }

    @Test
    void duplicateEmployeeIdOrEmailReturnsConflict() {
        when(employeeRepository.existsById("E-TEST")).thenReturn(true);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> peopleService.create(request("E-TEST", "person@example.com", "INR"), "hr"));
        assertEquals(409, exception.getStatusCode().value());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void missingEmployeeReturnsNotFound() {
        when(employeeRepository.findById("MISSING")).thenReturn(Optional.empty());
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> peopleService.employee("MISSING"));
        assertEquals(404, exception.getStatusCode().value());
    }

    @Test
    void updateRejectsPathIdThatDoesNotMatchRequestId() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> peopleService.update("E-1", request("E-2", "person@example.com", "INR"), "hr"));
        assertEquals(400, exception.getStatusCode().value());
        verify(employeeRepository, never()).findById(anyString());
    }

    @Test
    void deactivateMarksEmployeeInactiveAndWritesAudit() {
        Employee employee = new Employee("E-TEST", "Test User", "person@example.com", "Engineering", "Developer",
                "India", "INR", new BigDecimal("75000"), "Active", LocalDate.of(2024, 1, 1));
        when(employeeRepository.findById("E-TEST")).thenReturn(Optional.of(employee));

        assertEquals("Inactive", peopleService.deactivate("E-TEST", "hr").getStatus());
        verify(auditEventRepository).save(argThat(event -> "EMPLOYEE_DEACTIVATED".equals(event.getAction())));
    }

    @Test
    void countryRateCannotBeNegative() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> peopleService.updateCountry("IN", new BigDecimal("-1"), "admin"));
        assertEquals(400, exception.getStatusCode().value());
        verify(countryRepository, never()).findById(anyString());
    }

    private EmployeeRequest request(String id, String email, String currency) {
        return new EmployeeRequest(id, "Test User", email, "Engineering", "Developer", "India", currency,
                new BigDecimal("75000.00"), "Active", LocalDate.of(2024, 1, 1));
    }
}
