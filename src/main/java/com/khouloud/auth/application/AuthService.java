package com.khouloud.auth.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.khouloud.auth.dto.AuthRequest;
import com.khouloud.auth.dto.AuthResponse;
import com.khouloud.auth.security.jwt.JwtUtil;

@Service
public class AuthService {
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	UserDetailsService userDetailsService;

	JwtUtil jwtUtil;

	public AuthResponse login(AuthRequest request) {

		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));

		UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String token = JwtUtil.generateToken(userDetails.getUsername());

		return new AuthResponse(token);
	}
}
