package com.example.daily_health_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.daily_health_server.model.User;
import com.example.daily_health_server.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@PostMapping("/insert")
	public User insert(@RequestBody User user) {
		return userService.insert(user);
	}

}
