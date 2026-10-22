package com.peoplepay.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "salary_revisions", indexes = @Index(name = "idx_revision_event_at", columnList = "event_at"))
public class SalaryRevision {
	@Id
	@Column(length = 40)
	private String id;
	@Column(name = "employee_id", nullable = false, length = 30)
	private String employeeId;
	@Column(name = "old_salary", nullable = false, precision = 15, scale = 2)
	private BigDecimal oldSalary;
	@Column(name = "new_salary", nullable = false, precision = 15, scale = 2)
	private BigDecimal newSalary;
	@Column(nullable = false, length = 3)
	private String currency;
	@Column(name = "effective_date", nullable = false)
	private LocalDate effectiveDate;
	@Column(nullable = false, length = 500)
	private String reason;
	@Column(nullable = false, length = 100)
	private String username;
	@Column(name = "event_at", nullable = false)
	private LocalDateTime eventAt;

	protected SalaryRevision() {
	}

	public SalaryRevision(String id, String employeeId, BigDecimal oldSalary, BigDecimal newSalary, String currency,
			LocalDate effectiveDate, String reason, String username) {
		this.id = id;
		this.employeeId = employeeId;
		this.oldSalary = oldSalary;
		this.newSalary = newSalary;
		this.currency = currency;
		this.effectiveDate = effectiveDate;
		this.reason = reason;
		this.username = username;
		this.eventAt = LocalDateTime.now();
	}

	public String getId() {
		return id;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public BigDecimal getOldSalary() {
		return oldSalary;
	}

	public BigDecimal getNewSalary() {
		return newSalary;
	}

	public String getCurrency() {
		return currency;
	}

	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}

	public String getReason() {
		return reason;
	}

	public String getUsername() {
		return username;
	}

	public LocalDateTime getEventAt() {
		return eventAt;
	}
}
