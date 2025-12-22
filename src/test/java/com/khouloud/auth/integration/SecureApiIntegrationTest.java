package com.khouloud.auth.integration;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import com.khouloud.auth.model.Role;
import com.khouloud.auth.model.User;

public class SecureApiIntegrationTest extends BaseIntegrationTest {

	private static String mockedToken;

	@BeforeEach
	void setUp() {
		var mockedRole = Role.builder().name("ADMIN").build();
		var mockedUser = User.builder().password("test").email("test2")
				.roles(new HashSet<>(Arrays.asList(mockedRole))).build();
		when(userRepository.findByEmail("test2")).thenReturn(Optional.of(mockedUser));
		mockedToken = jwtService.generateToken(mockedUser);
	}

	@Test
	void testAccessSecuredEndpoint_withoutToken_shouldReturn403() throws Exception {
		mockMvc.perform(get("/api/secure").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	
	@Test
	void testAccessSecuredEndpoint_withValidToken() throws Exception {
		mockMvc.perform(
				get("/api/secure").header("Authorization", "Bearer " + mockedToken)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
}
