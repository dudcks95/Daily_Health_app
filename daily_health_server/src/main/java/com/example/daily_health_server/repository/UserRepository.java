package com.example.daily_health_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.daily_health_server.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

}
