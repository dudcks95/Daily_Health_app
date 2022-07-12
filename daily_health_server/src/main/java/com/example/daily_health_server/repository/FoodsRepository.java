package com.example.daily_health_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.daily_health_server.model.Foods;

public interface FoodsRepository extends JpaRepository<Foods, Long>{
	List<Foods> findByFoodNameContaining(String foodName);


}
