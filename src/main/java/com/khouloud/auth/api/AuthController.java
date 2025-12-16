package com.khouloud.auth.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khouloud.auth.application.AuthService;
import com.khouloud.auth.dto.AuthRequest;
import com.khouloud.auth.dto.AuthResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
	  private final AuthService authService;

	    public AuthController(AuthService authService) {
	        this.authService = authService;
	    }

	    @PostMapping("/login")
	    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
	        AuthResponse response = authService.login(request);
	        return ResponseEntity.ok(response);
	    }
}
