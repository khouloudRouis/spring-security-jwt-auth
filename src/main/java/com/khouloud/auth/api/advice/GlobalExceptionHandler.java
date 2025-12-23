package com.khouloud.auth.api.advice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.jsonwebtoken.ExpiredJwtException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorResponse> handleWebClientException(BadCredentialsException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),"Invalid email or password", null));
	}
 
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
		return ResponseEntity.status(HttpStatus.FORBIDDEN)
				.body(new ErrorResponse(HttpStatus.FORBIDDEN.value(),"You do not have permission to access this resource", null));

	}
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorResponse> handleExpiredJwtException(ExpiredJwtException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),ex.getMessage(), null));

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(
	        MethodArgumentNotValidException ex) {

	    Map<String, String> errors = new HashMap<>();

	    ex.getBindingResult().getFieldErrors().forEach(error ->
	            errors.put(error.getField(), error.getDefaultMessage())
	    );

	    ErrorResponse response = new ErrorResponse(
	            HttpStatus.BAD_REQUEST.value(),
	            "Validation failed",
	            errors
	    );

	    return ResponseEntity
	            .status(HttpStatus.BAD_REQUEST)
	            .body(response);
	}

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage(), null));

	}
	
	 
}
