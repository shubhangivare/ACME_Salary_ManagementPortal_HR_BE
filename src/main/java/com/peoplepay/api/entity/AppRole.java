package com.peoplepay.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_roles")
public class AppRole {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true, length = 30)
	private String name;

	protected AppRole() {
	}

	public AppRole(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
