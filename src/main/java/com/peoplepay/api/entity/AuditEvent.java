package com.peoplepay.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "audit_events")
public class AuditEvent {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "event_at", nullable = false)
	private LocalDateTime eventAt;
	@Column(nullable = false, length = 100)
	private String username;
	@Column(nullable = false, length = 80)
	private String action;
	@Column(nullable = false, length = 1000)
	private String detail;

	protected AuditEvent() {
	}

	public AuditEvent(String username, String action, String detail) {
		this.eventAt = LocalDateTime.now();
		this.username = username;
		this.action = action;
		this.detail = detail;
	}

	public Long getId() {
		return id;
	}

	public LocalDateTime getEventAt() {
		return eventAt;
	}

	public String getUsername() {
		return username;
	}

	public String getAction() {
		return action;
	}

	public String getDetail() {
		return detail;
	}
}
