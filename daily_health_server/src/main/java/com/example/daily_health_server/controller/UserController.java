package com.example.daily_health_server.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@GetMapping("/list/{email}")
	public User list(@PathVariable String email){
		return userService.list(email);
	}
	
	@PutMapping("/update/{email}")
	public User update(@PathVariable String email, @RequestBody User user) {
		return userService.update(email, user);
	}
	
	@DeleteMapping("/delete/{email}")
	public void delete(@PathVariable String email) {
		userService.delete(email);
	}

}
