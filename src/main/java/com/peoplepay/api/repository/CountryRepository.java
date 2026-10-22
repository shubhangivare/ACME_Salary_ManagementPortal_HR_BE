package com.peoplepay.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peoplepay.api.entity.Country;

public interface CountryRepository extends JpaRepository<Country, String> {
}
