package com.khouloud.auth.service;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khouloud.auth.dto.AuthRequest;
import com.khouloud.auth.dto.AuthResponse;
import com.khouloud.auth.dto.RegisterRequest;
import com.khouloud.auth.model.Role;
import com.khouloud.auth.model.User;
import com.khouloud.auth.repository.RoleRepository;
import com.khouloud.auth.repository.UserRepository;
import com.khouloud.auth.security.JwtService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

	private final AuthenticationManager authenticationManager;
	private final UserDetailsService userDetailsService;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;

	public AuthResponse login(AuthRequest request) {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password()));
		var userDetails = userDetailsService.loadUserByUsername(request.email());
		var jwt = jwtService.generateToken(userDetails);
		return new AuthResponse(jwt);
	}

	public AuthResponse register(RegisterRequest request) {
		Set<Role> roles = request.roles().stream()
				.map(roleName -> roleRepository.findByName(roleName)
						.orElseThrow(() -> new RuntimeException("Role not found: " + roleName)))
				.collect(Collectors.toSet());

		var user = User.createUser(request.fullName(), request.email(), 
				passwordEncoder.encode(request.password()), roles);

		userRepository.save(user);
		
		var jwt = jwtService.generateToken(user);
		return new AuthResponse(jwt);

	}
}
