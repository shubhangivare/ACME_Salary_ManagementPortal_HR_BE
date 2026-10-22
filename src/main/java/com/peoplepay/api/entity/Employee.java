package com.peoplepay.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employees", indexes = { @Index(name = "idx_employee_name", columnList = "name"),
		@Index(name = "idx_employee_department", columnList = "department") })
public class Employee {
	@Id
	@Column(length = 30)
	private String id;
	@Column(nullable = false, length = 120)
	private String name;
	@Column(nullable = false, unique = true, length = 180)
	private String email;
	@Column(nullable = false, length = 100)
	private String department;
	@Column(nullable = false, length = 120)
	private String designation;
	@Column(nullable = false, length = 100)
	private String country;
	@Column(nullable = false, length = 3)
	private String currency;
	@Column(nullable = false, precision = 15, scale = 2)
	private BigDecimal salary;
	@Column(nullable = false, length = 12)
	private String status;
	@Column(nullable = false)
	private LocalDate joined;

	protected Employee() {
	}

	public Employee(String id, String name, String email, String department, String designation, String country,
			String currency, BigDecimal salary, String status, LocalDate joined) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.department = department;
		this.designation = designation;
		this.country = country;
		this.currency = currency;
		this.salary = salary;
		this.status = status;
		this.joined = joined;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public String getDepartment() {
		return department;
	}

	public String getDesignation() {
		return designation;
	}

	public String getCountry() {
		return country;
	}

	public String getCurrency() {
		return currency;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public String getStatus() {
		return status;
	}

	public LocalDate getJoined() {
		return joined;
	}

	public void updateFrom(Employee e) {
		name = e.name;
		email = e.email;
		department = e.department;
		designation = e.designation;
		country = e.country;
		currency = e.currency;
		salary = e.salary;
		status = e.status;
		joined = e.joined;
	}

	public void setSalary(BigDecimal salary) {
		this.salary = salary;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
