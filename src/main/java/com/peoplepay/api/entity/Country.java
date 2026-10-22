package com.peoplepay.api.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "countries")
public class Country {
	@Id
	@Column(length = 2)
	private String code;
	@Column(nullable = false, length = 100)
	private String name;
	@Column(nullable = false, length = 3)
	private String currency;
	@Column(nullable = false, length = 12)
	private String symbol;
	@Column(name = "rate_to_inr", nullable = false, precision = 12, scale = 4)
	private BigDecimal rateToInr;
	@Column(nullable = false)
	private LocalDate updated;

	protected Country() {
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}

	public String getCurrency() {
		return currency;
	}

	public String getSymbol() {
		return symbol;
	}

	public BigDecimal getRateToInr() {
		return rateToInr;
	}

	public LocalDate getUpdated() {
		return updated;
	}

	public void updateRate(BigDecimal rate) {
		rateToInr = rate;
		updated = LocalDate.now();
	}
}
