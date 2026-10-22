package com.peoplepay.api.controller;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException ex) {
		String details = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getField() + ": "
						+ (error.getDefaultMessage() == null ? "invalid value" : error.getDefaultMessage()))
				.distinct().collect(Collectors.joining("; "));
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("message", "Validation failed. Please correct the highlighted fields.");
		body.put("detail", details);
		body.put("errors",
				ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(error -> error.getField(),
						error -> error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage(),
						(first, second) -> first, LinkedHashMap::new)));
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(ResponseStatusException.class)
	public ResponseEntity<Map<String, Object>> status(ResponseStatusException ex) {
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("message", ex.getReason() == null ? "Request could not be completed." : ex.getReason());
		body.put("detail", body.get("message"));
		return ResponseEntity.status(ex.getStatusCode()).body(body);
	}

	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<Map<String, Object>> conflict(Exception ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(Map.of("message", "The request conflicts with existing data.", "detail",
						"A record with the same unique identifier or related data already exists."));
	}

	@ExceptionHandler({ MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class,
			IllegalArgumentException.class, HttpMessageNotReadableException.class })
	public ResponseEntity<Map<String, Object>> badRequest(Exception ex) {
		String detail = ex instanceof MissingServletRequestParameterException
				? "A required request parameter is missing."
				: "One or more request parameters are invalid.";
		return ResponseEntity.badRequest().body(Map.of("message", "Invalid request.", "detail", detail));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Map<String, Object>> unexpected(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(Map.of("message", "An unexpected server error occurred. Please try again.", "detail",
						"An unexpected server error occurred. Please try again."));
	}
}
