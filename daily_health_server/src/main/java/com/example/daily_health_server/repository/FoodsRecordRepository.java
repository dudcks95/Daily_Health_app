package com.example.daily_health_server.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.daily_health_server.model.FoodsRecord;

public interface FoodsRecordRepository extends JpaRepository<FoodsRecord, Long>{
//	@Query(value = "select fr from foods_record fr where userid=?1 and month=?2 and day=?3 and eat_time=?4")
//	public List<FoodsRecord> selectfoodsRecord(Long userid, int month, String day, Long eatTime);
	
	@Query(value = "select fr from foods_record fr where userid=?1 and month=?2 and day=?3")
	public List<FoodsRecord> selectfoodsRecord(Long userid, int month, String day);
	
	@Query(value = "select fr from foods_record fr where userid=?1 and month=?2")
	public List<FoodsRecord> sumkcal2(Long userid, int month);

}
