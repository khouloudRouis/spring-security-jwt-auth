package com.khouloud.auth.api.exception;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleWebClientException(BadCredentialsException ex,
			HttpServletRequest request) {

		log.warn("AUTH FAILURE: Invalid login attempt for IP {} at {}", request.getRemoteAddr(), Instant.now());

		var message = "Invalid email or password";
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
				message, Instant.now(), request.getRequestURI(), null));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		log.warn("VALIDATION FAILED: {} at {}", errors, Instant.now());

		var message = "Validation failed";
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, Instant.now(),
				request.getRequestURI(), errors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(UserAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex,
			HttpServletRequest request) {
		log.warn("UserAlreadyExistsException at [{}]: {}", request.getRequestURI(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(),
				ex.getMessage(), Instant.now(), request.getRequestURI(), null));

	}

	@ExceptionHandler(RoleNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleRoleNotFound(RoleNotFoundException ex, HttpServletRequest request) {

		log.warn("RoleNotFoundException at [{}]: {}", request.getRequestURI(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
				ex.getMessage(), Instant.now(), request.getRequestURI(), null));

	}

}
