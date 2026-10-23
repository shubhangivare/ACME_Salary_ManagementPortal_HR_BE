package com.peoplepay.api.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "app_users", uniqueConstraints = @UniqueConstraint(columnNames = "username"))
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false, unique = true, length = 50)
	private String username;
	@Column(name = "password_hash", nullable = false, length = 100)
	private String passwordHash;
	@Column(name = "display_name", nullable = false, length = 100)
	private String displayName;
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "role_id", nullable = false)
	private AppRole role;
	@Column(nullable = false)
	private boolean enabled = true;

	protected AppUser() {
	}

	public AppUser(String username, String passwordHash, String displayName, AppRole role) {
		this.username = username;
		this.passwordHash = passwordHash;
		this.displayName = displayName;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String value) {
		passwordHash = value;
	}

	public String getDisplayName() {
		return displayName;
	}

	public AppRole getRole() {
		return role;
	}

	public boolean isEnabled() {
		return enabled;
	}
}
