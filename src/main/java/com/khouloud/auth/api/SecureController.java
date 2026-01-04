package com.khouloud.auth.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.khouloud.auth.dto.UserDto;
import com.khouloud.auth.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/secure")
public class SecureController {
	private final UserService userService;

	@GetMapping
	public ResponseEntity<List<UserDto>> getAllUsers() {
		log.info("Get all users requested");
		var users = userService.getAllUsers();
		log.info("Get all users completed, count={}", users.size());
		return ResponseEntity.ok(users);
	}

}
