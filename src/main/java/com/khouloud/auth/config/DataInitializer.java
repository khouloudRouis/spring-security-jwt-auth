package com.khouloud.auth.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.khouloud.auth.model.Role;
import com.khouloud.auth.model.User;
import com.khouloud.auth.repository.RoleRepository;
import com.khouloud.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;
@Profile("dev")
@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {
	private final RoleRepository roleRepository;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		var userRole = roleRepository.findByName("USER").orElseGet(() -> roleRepository.save(new Role("USER")));

		var adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> roleRepository.save(new Role("ADMIN")));

		List<User> users = new ArrayList<>();
		if (!userRepository.findByEmail("user@email").isPresent()) {
			User user = User.createUser("Default User", "user@email", passwordEncoder.encode("change-me"),
					Set.of(userRole));
			users.add(user);

		}
		if (!userRepository.findByEmail("admin@email").isPresent()) {
			User admin = User.createUser("System Administrator", "admin@email", passwordEncoder.encode("change-me"),
					Set.of(adminRole, userRole));
			users.add(admin);
		}

		userRepository.saveAll(users);

	}
}
