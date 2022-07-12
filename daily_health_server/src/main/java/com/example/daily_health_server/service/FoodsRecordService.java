package com.example.daily_health_server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.daily_health_server.model.FoodsRecord;
import com.example.daily_health_server.repository.FoodsRecordRepository;

@Service
public class FoodsRecordService {
	@Autowired
	private FoodsRecordRepository foodsRecordRepository;
	
	public FoodsRecord insert(FoodsRecord foodsRecord) {
		return foodsRecordRepository.save(foodsRecord);
	}
}
