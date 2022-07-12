package com.example.daily_health_server.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.daily_health_server.model.Foods;
import com.example.daily_health_server.repository.FoodsRepository;

@Service
public class FoodsService {
	@Autowired
	private FoodsRepository foodsRepository;
	
	
	public List<Foods> search(String foodName){
		List<Foods> foodsList = foodsRepository.findByFoodNameContaining(foodName);
		return foodsList;
	}
	
	
	public List<Foods> list(){
		return foodsRepository.findAll();
	}
}
