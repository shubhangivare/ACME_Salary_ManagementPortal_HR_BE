package com.peoplepay.api.dto;
import jakarta.validation.constraints.*; import java.math.BigDecimal; import java.time.LocalDate;
public record RevisionRequest(@NotBlank String employeeId,@NotNull @DecimalMin("0.0") BigDecimal newSalary,@NotNull LocalDate effectiveDate,@NotBlank @Size(max=500) String reason) {}
