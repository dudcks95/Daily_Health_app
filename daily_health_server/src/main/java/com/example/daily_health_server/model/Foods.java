package com.example.daily_health_server.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "foods")
public class Foods {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long foodId;
	private String foodName;
	private Long foodSize; // 총내용량
	private Long kcal; // 칼로리
	private Long carbohydrate; // 탄수화물
	private Long protein; // 단백질
	private Long fat; // 지방
	private Long natrium; // 나트륨
	private Long cholesterol;  // 콜레스테롤

}
