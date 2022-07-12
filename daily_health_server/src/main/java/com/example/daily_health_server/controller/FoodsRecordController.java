package com.example.daily_health_server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.daily_health_server.model.AnFoods;
import com.example.daily_health_server.model.Foods;
import com.example.daily_health_server.model.FoodsRecord;
import com.example.daily_health_server.model.User;
import com.example.daily_health_server.service.FoodsRecordService;

@RestController
public class FoodsRecordController {
	@Autowired
	private FoodsRecordService foodsRecordService;
	
	@PostMapping("/foodinsert")
	public FoodsRecord foodinsert(@RequestBody AnFoods anfood) {
		FoodsRecord foodRecord = new FoodsRecord();
		Foods food = new Foods();
		
		food.setFoodId(anfood.getFoodId());
		
		User user = new User();
		user.setUserid(Long.valueOf(anfood.getUserid()));
		
		foodRecord.setFood(food);
		foodRecord.setMonth(anfood.getMonth());
		foodRecord.setDay(anfood.getDay());
		foodRecord.setUser(user);
		foodRecord.setEatTime(anfood.getEatTime());
		return foodsRecordService.insert(foodRecord);
	}
}
