package com.example.daily_health_server.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnFoods {
	 private Long foodRecordId;
	    private Long foodId; // foods 기본키
	    private String foodName;
	    private int month; // 월
	    private String day; // 일
	    private Long eatTime; // 아침:1, 점심:2, 저녁:3
	    private Long userid; // 유저 기본키
	    private Long kcal;
}
