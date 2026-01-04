package com.khouloud.auth.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
 
public record RegisterRequest(@NotBlank String fullName, @Email String email, @NotBlank String password,@NotEmpty Set<String> roles) {}
