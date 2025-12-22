package com.khouloud.auth.dto;

import java.util.Set;
 
public record RegisterRequest(String fullName, String email, String password, Set<String> roles) {}
