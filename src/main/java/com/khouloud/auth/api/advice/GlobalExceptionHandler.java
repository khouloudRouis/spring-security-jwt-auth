package com.khouloud.auth.api.advice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleWebClientException(BadCredentialsException ex,
			HttpServletRequest request) {

		logger.warn("AUTH FAILURE: Invalid login attempt for IP {} at {}", request.getRemoteAddr(), Instant.now());

		var message = "Invalid email or password";
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
				message, Instant.now(), request.getRequestURI(), null));
	}

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request) {
		logger.info("JWT EXPIRED for request {} at {}", request.getRequestURI(), Instant.now());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
				ex.getMessage(), Instant.now(), request.getRequestURI(), null));

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		Map<String, String> errors = new HashMap<>();

		ex.getBindingResult().getFieldErrors()
				.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

		logger.warn("VALIDATION FAILED: {} at {}", errors, Instant.now());

		var message = "Validation failed";
		ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message, Instant.now(),
				request.getRequestURI(), errors);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {

		logger.error("UNEXPECTED ERROR at {}", Instant.now(), ex);

		var message = "An unexpected error occurred";
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(), message, Instant.now(), request.getRequestURI(), null));

	}

}
