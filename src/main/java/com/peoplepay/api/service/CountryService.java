package com.peoplepay.api.service;

import java.math.BigDecimal;
import java.util.List;

import com.peoplepay.api.entity.Country;

public interface CountryService {
	List<Country> countries();

	Country updateCountry(String code, BigDecimal rate, String username);
}
