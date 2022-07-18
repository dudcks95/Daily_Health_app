package com.example.daily_health_server.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.daily_health_server.model.FoodsRecord;
import com.example.daily_health_server.repository.FoodsRecordRepository;

@Service
public class FoodsRecordService {
	@Autowired
	private FoodsRecordRepository foodsRecordRepository;
	
	public FoodsRecord insert(FoodsRecord foodsRecord) {
		return foodsRecordRepository.save(foodsRecord);
	}
	
//	public List<FoodsRecord> selectfoodsRecord(Long userid, int month, String day, Long eatTime){
//		return foodsRecordRepository.selectfoodsRecord(userid, month, day, eatTime);
//	}
	
	public List<FoodsRecord> findAll(){
		return foodsRecordRepository.findAll();
	}
	
	public List<FoodsRecord> selectfoodsRecord (Long userid, int month, String day){
		return foodsRecordRepository.selectfoodsRecord(userid, month, day);
	}
	
	public List<FoodsRecord> sumkcal2 (Long userid, int month){
		return foodsRecordRepository.sumkcal2(userid, month);
	}
}
