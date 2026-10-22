package com.peoplepay.api.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.peoplepay.api.entity.Country;
import com.peoplepay.api.service.CountryService;
import com.peoplepay.api.service.PeopleService;

@Service
public class CountryServiceImpl implements CountryService {
	private final PeopleService peopleService;

	public CountryServiceImpl(PeopleService peopleService) {
		this.peopleService = peopleService;
	}

	@Override
	public List<Country> countries() {
		return peopleService.countries();
	}

	@Override
	public Country updateCountry(String code, BigDecimal rate, String username) {
		return peopleService.updateCountry(code, rate, username);
	}
}
