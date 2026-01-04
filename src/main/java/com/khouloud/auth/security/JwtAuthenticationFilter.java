package com.khouloud.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			final String jwt = authHeader.substring(7);
			final String userName = jwtService.extractUsername(jwt);

			if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				try {
					UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
					if (jwtService.isTokenValid(jwt, userDetails)) {
						var authToken = new UsernamePasswordAuthenticationToken(userDetails, null,
								userDetails.getAuthorities());
						authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						SecurityContextHolder.getContext().setAuthentication(authToken);
						log.debug("JWT token validated successfully for user: {}", userName);
					}
				} catch (UsernameNotFoundException ex) {
					log.warn("User not found: {} ", userName);
				}
			}
		} catch (JwtException ex) {
			log.warn("Invalid JWT token from IP: {} for URI: {} - {}", request.getRemoteAddr(),
					request.getRequestURI(), ex.getClass().getSimpleName());
		} catch (Exception ex) {
			log.error("Unexpected error processing JWT token from IP: {} for URI: {}", request.getRemoteAddr(),
					request.getRequestURI(), ex);
		}

		filterChain.doFilter(request, response);
	}

}
