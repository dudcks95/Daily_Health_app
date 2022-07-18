package com.example.daily_health_server.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "foods_record")
public class FoodsRecord {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long foodRecordId;
	
	@ManyToOne
	@JoinColumn(name = "food_id")
	private Foods food;
	
//	private Date oneDay;
	private int month;
	private String day;
	private Long eatTime;
	
	@ManyToOne
	@JoinColumn(name = "userid")
	private User user;
}
