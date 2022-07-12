package com.example.daily_health_server.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.daily_health_server.model.FoodsRecord;

public interface FoodsRecordRepository extends JpaRepository<FoodsRecord, Long>{


}
