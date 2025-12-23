package com.khouloud.auth.integration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import com.khouloud.auth.dto.AuthRequest;
import com.khouloud.auth.dto.RegisterRequest;
import com.khouloud.auth.model.Role;
import com.khouloud.auth.model.User;



public class AuthApiIntegrationTest extends BaseIntegrationTest {

	@Test
	void testAccessLoginEndpoint_WithKnownUser_ShouldReturn200() throws Exception {
		var password ="$2a$10$JnEShy0Smx1aP9n.N6ct3eKViHKPbdbojTTTREEVAKe8miTaKgkWi";
		var mockedRole = Role.builder().name("USER").build();
		var mockedUser = User.builder().email("user_test").password(password)
				.roles(new HashSet<>(Arrays.asList(mockedRole))).build();	
		var request = new AuthRequest("user_test","user_test");
		
		when(userRepository.findByEmail(any())).thenReturn(Optional.of(mockedUser));
		when(roleRepository.findByName(any())).thenReturn(Optional.of(mockedRole));
		
		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk());
	}
	
	@Test
	void testAccessLoginEndpoint_WithUnkownUser_ShouldReturn401() throws Exception {
		var mockedRequest = new AuthRequest("unkown", "unkown");
		
		when(userRepository.findByEmail("test2")).thenReturn(Optional.empty());
	
		mockMvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mockedRequest)))
		.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAccessRegisterEndpoint_WithNoRoles_ShouldReturn400() throws Exception {
		var mockedRequest = new RegisterRequest("test full name","test@test","test", null);
		
		when(userRepository.save(any())).thenReturn(new User());
		when(roleRepository.findByName(any())).thenReturn(Optional.empty());
		
		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mockedRequest)))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	void testAccessRegisterEndpoint_WithCorrectUser_ShouldReturn201() throws Exception {
		when(roleRepository.findByName(any())).thenReturn(Optional.of(new Role()));
		when(userRepository.save(any())).thenReturn(new User());
		var mockedRequest = new RegisterRequest("test full name","test@test","test", new HashSet<>(List.of("USER")));
 
		mockMvc.perform(post("/api/auth/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(mockedRequest)))
		.andExpect(status().isCreated());
	}
	
	@Test
	void testAccessUnkownEndpoint_Should500() throws Exception {
		mockMvc.perform(
				get("/api/auth/wrong")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isInternalServerError());
	}
}
