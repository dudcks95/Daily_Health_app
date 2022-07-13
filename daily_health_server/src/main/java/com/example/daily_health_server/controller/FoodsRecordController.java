package com.example.daily_health_server.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@GetMapping("/selectfoodsRecord/{userid}/{month}/{day}/{eatTime}")
	public List<Foods> selectfoodsRecord(@PathVariable Long userid, @PathVariable int month, @PathVariable String day, @PathVariable Long eatTime){
//		foodsRecordService.findAll();
		List<FoodsRecord> fr = foodsRecordService.selectfoodsRecord(userid, month, day, eatTime);
		List<Foods> fd = new ArrayList<Foods>();
		for(int i = 0; i<fr.size(); i++) {
			Foods food = fr.get(i).getFood();	
			
			fd.add(food);
			System.out.println(food.getFoodName());
			System.out.println(fr.get(i).getUser().getUserid());
		}
		
		
		return fd;
	}
}
