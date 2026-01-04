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

import io.jsonwebtoken.ExpiredJwtException;
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

	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request) {
		log.info("JWT EXPIRED for request {} at {}", request.getRequestURI(), Instant.now());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),
				ex.getMessage(), Instant.now(), request.getRequestURI(), null));

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

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {

		log.error("UNEXPECTED ERROR at {}", Instant.now(), ex);

		var message = "An unexpected error occurred";
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
				HttpStatus.INTERNAL_SERVER_ERROR.value(), message, Instant.now(), request.getRequestURI(), null));

	}
	  @ExceptionHandler(UserAlreadyExistsException.class)
	    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex, HttpServletRequest request) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(
					HttpStatus.CONFLICT.value(), ex.getMessage(), Instant.now(), request.getRequestURI(), null));

	    }
	  @ExceptionHandler(RoleNotFoundException.class)
	    public ResponseEntity<ErrorResponse> handleRoleNotFound(RoleNotFoundException ex, HttpServletRequest request) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(
					HttpStatus.NOT_FOUND.value(), ex.getMessage(), Instant.now(), request.getRequestURI(), null));

	    }
	 

}
