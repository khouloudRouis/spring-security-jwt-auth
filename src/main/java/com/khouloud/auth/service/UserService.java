package com.khouloud.auth.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.khouloud.auth.dto.UserDto;
import com.khouloud.auth.mapper.UserMapper;
import com.khouloud.auth.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final UserMapper mapper;
	
	public List<UserDto> getAllUsers(){
		return userRepository.findAll().stream().map(mapper::toDto).toList();
	}

}
