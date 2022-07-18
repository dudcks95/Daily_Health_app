package com.example.daily_health_server.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

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
	
	public User list(String email){
		return userRepository.findByEmail(email);
	}
	
	@Transactional
	public User update(String email, User user) {
		User u = userRepository.findByEmail(email);
		
		u.setUsername(user.getUsername());
		u.setHeight(user.getHeight());
		u.setWeight(user.getWeight());
		u.setGender(user.getGender());
		return u;
	}
	
	public void delete(String email) {
		userRepository.deleteByEmail(email);
	}

}
