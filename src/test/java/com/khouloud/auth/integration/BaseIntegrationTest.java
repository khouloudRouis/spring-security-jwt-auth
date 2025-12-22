package com.khouloud.auth.integration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khouloud.auth.repository.RoleRepository;
import com.khouloud.auth.repository.UserRepository;
import com.khouloud.auth.security.JwtService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {
	@Autowired
	protected MockMvc mockMvc;
	@Autowired
	protected JwtService jwtService;
	@Autowired
	protected ObjectMapper objectMapper;
	@MockitoBean
	protected UserRepository userRepository;
	@MockitoBean
	protected RoleRepository roleRepository;
}
