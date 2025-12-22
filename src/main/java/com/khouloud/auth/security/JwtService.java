package com.khouloud.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
 
@Service
public class JwtService {
	
	@Value("${security.jwt.secret-key}")
    private String jwtSecret ;
    @Value("${security.jwt.expiration-time}") 
    private long expirationTime;
   
    public String generateToken(
    		UserDetails userDetails) {
        return this.generateToken(new HashMap<>(),userDetails);
    }
    
    public String generateToken(Map<String,Object> claims,
    		UserDetails userDetails) {
    		  return Jwts.builder()
    	        		.setClaims(claims)
    	                .setSubject(userDetails.getUsername())
    	                .setIssuedAt(new Date(System.currentTimeMillis()))
    	                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
    	                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
    	                .compact();
      
    }

    private Key getSigningKey() {
    	  return  Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    private <T> T extractClaim(
            String token,
            Function<Claims, T> claimsResolver
    ) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
    	 return extractClaim(token, Claims::getSubject);
    }
    
}


