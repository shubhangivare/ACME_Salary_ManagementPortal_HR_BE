package com.peoplepay.api.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.peoplepay.api.entity.Country;
import com.peoplepay.api.service.CountryService;


@RestController
@RequestMapping("/api/countries")
public class CountryController {
	private final CountryService countryService;

	public CountryController(CountryService countryService) {
		this.countryService = countryService;
	}

	private Map<String, Object> toMap(Country country) {
		return Map.of("name", country.getName(), "code", country.getCode(), "currency", country.getCurrency(), "symbol",
				country.getSymbol(), "rate", country.getRateToInr(), "updated", country.getUpdated());
	}

	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> getCountries() {
		return ResponseEntity.ok(countryService.countries().stream().map(this::toMap).toList());
	}

	@PutMapping("/{code}")
	public ResponseEntity<Map<String, Object>> updateCountry(@PathVariable String code,
			@RequestBody Map<String, BigDecimal> request, Authentication authentication) {
		return ResponseEntity
				.ok(toMap(countryService.updateCountry(code, request.get("rate"), authentication.getName())));
	}
}
