package com.peoplepay.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequest(@NotBlank @Size(max = 30) @Pattern(regexp = "[A-Za-z0-9_-]+") String id,
		@NotBlank @Size(max = 120) String name, @NotBlank @Email @Size(max = 180) String email,
		@NotBlank @Size(max = 100) String department, @NotBlank @Size(max = 120) String designation,
		@NotBlank @Size(max = 100) String country, @NotBlank @Size(min = 3, max = 3) String currency,
		@NotNull @DecimalMin("0.0") @Digits(integer = 13, fraction = 2) BigDecimal salary,
		@NotBlank @Pattern(regexp = "Active|Inactive") String status, @NotNull LocalDate joined) {
}
