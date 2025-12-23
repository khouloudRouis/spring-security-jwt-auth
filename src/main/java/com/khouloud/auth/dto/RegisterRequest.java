package com.khouloud.auth.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
 
public record RegisterRequest(String fullName, @NotBlank String email, @NotBlank String password,@NotEmpty Set<String> roles) {}
