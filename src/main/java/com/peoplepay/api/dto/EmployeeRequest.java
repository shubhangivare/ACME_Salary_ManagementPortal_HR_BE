package com.peoplepay.api.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record EmployeeRequest(@NotBlank String id, @NotBlank @Size(max = 120) String name,
		@NotBlank @Email String email, @NotBlank String department, @NotBlank String designation,
		@NotBlank String country, @NotBlank @Size(min = 3, max = 3) String currency,
		@NotNull @DecimalMin("0.0") BigDecimal salary, @NotBlank @Pattern(regexp = "Active|Inactive") String status,
		@NotNull LocalDate joined) {
}
