package com.example.daily_health_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.daily_health_server.model.User;
import com.example.daily_health_server.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	public User insert(User user) {
		return userRepository.save(user);
	}

}
