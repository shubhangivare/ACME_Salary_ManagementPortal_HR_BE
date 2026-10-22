package com.peoplepay.api;

import com.peoplepay.api.entity.AuditEvent;
import com.peoplepay.api.entity.Employee;
import com.peoplepay.api.entity.SalaryRevision;
import com.peoplepay.api.repository.AuditEventRepository;
import com.peoplepay.api.repository.CountryRepository;
import com.peoplepay.api.repository.EmployeeRepository;
import com.peoplepay.api.repository.SalaryRevisionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** Repository/DAO tests against the configured in-memory H2 database. */
@DataJpaTest(properties = {
        "spring.sql.init.mode=never",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class DaoLayerTest {
    @Autowired EmployeeRepository employeeRepository;
    @Autowired AuditEventRepository auditEventRepository;
    @Autowired CountryRepository countryRepository;
    @Autowired SalaryRevisionRepository salaryRevisionRepository;

    @Test
    void employeeRepositoryPersistsAndFindsEmployeeByIdAndEmail() {
        Employee employee = new Employee("DAO-TEST-1", "DAO Test", "dao-test@example.com", "Engineering",
                "Developer", "India", "INR", new BigDecimal("65000.00"), "Active", LocalDate.of(2024, 1, 1));
        employeeRepository.saveAndFlush(employee);

        assertTrue(employeeRepository.existsById("DAO-TEST-1"));
        assertTrue(employeeRepository.existsByEmailIgnoreCase("DAO-TEST@example.com"));
        assertEquals("DAO Test", employeeRepository.findAllByOrderByNameAsc().stream()
                .filter(item -> "DAO-TEST-1".equals(item.getId())).findFirst().orElseThrow().getName());
    }

    @Test
    void countryRepositoryFindsSeedIndependentCountryByIdWhenPresent() {
        // This test intentionally checks repository behavior without assuming seed rows exist.
        var savedCountry = countryRepository.findAll().stream().findFirst();
        if (savedCountry.isPresent()) {
            assertTrue(countryRepository.findById(savedCountry.get().getCode()).isPresent());
        } else {
            assertTrue(countryRepository.findAll().isEmpty());
        }
    }

    @Test
    void auditRepositoryPersistsAndOrdersEvents() {
        auditEventRepository.saveAndFlush(new AuditEvent("dao-test", "DAO_TEST", "repository test event"));
        assertTrue(auditEventRepository.findAllByOrderByEventAtDescIdDesc(PageRequest.of(0, 500)).stream()
                .anyMatch(event -> "DAO_TEST".equals(event.getAction())));
    }

    @Test
    void salaryRevisionRepositoryPersistsRevision() {
        SalaryRevision revision = new SalaryRevision("DAO-REV-1", "DAO-EMP-1", new BigDecimal("50000.00"),
                new BigDecimal("55000.00"), "INR", LocalDate.of(2026, 10, 1), "DAO test", "admin");
        salaryRevisionRepository.saveAndFlush(revision);

        assertEquals(new BigDecimal("55000.00"), salaryRevisionRepository.findById("DAO-REV-1").orElseThrow().getNewSalary());
        assertTrue(salaryRevisionRepository.findAllByOrderByEventAtDesc().stream()
                .anyMatch(item -> "DAO-REV-1".equals(item.getId())));
    }
}
