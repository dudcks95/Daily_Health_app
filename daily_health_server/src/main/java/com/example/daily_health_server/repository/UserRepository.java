package com.example.daily_health_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.daily_health_server.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	User findByEmail(String email);
	
	@Query(value ="delete from dailyhealth where email=?1", nativeQuery = true)
	Void deleteByEmail(String email);
}
