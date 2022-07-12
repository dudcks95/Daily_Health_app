package com.example.daily_health_server.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.daily_health_server.model.Foods;
import com.example.daily_health_server.repository.FoodsRepository;
import com.example.daily_health_server.service.FoodsService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class FoodsController {
	@Autowired
	private FoodsService foodsService;
	@Autowired
	private FoodsRepository foodsRepository;
	
	@GetMapping("/")
	public String home() {
		return "ok";
	}
	
	@GetMapping("/api")
	public String api() {
		String result = "";
		try {
			URL url = new URL("http://openapi.foodsafetykorea.go.kr/api/549d13f5e20f49e38973/I2790/json/1/20");
			HttpURLConnection urlconnection = (HttpURLConnection) url.openConnection();
			
			//request header 쪽
			urlconnection.setRequestMethod("GET");
			BufferedReader br = null;
			int responseCode = urlconnection.getResponseCode(); //성공, 실패시 반환 코드
			if(responseCode == 200) { // 성공 시
				br = new BufferedReader(new InputStreamReader(urlconnection.getInputStream(), "UTF-8"));
			}
			
			String line;
			while((line = br.readLine()) != null) { // 한줄 씩 읽기
				result = result + line + "\n";
			}
			
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode jsonNode = objectMapper.readTree(result);
			List<Foods> foods = new ArrayList<>(); // foods 리스트
			for (JsonNode node : jsonNode.get("I2790").get("row")) { // get(서비스명(여기선 I2790))안에 get(row)의 데이터
				Foods food = new Foods();
				food.setFoodName(node.get("DESC_KOR").asText());
				food.setFoodSize(node.get("SERVING_SIZE").asLong());
				food.setKcal(node.get("NUTR_CONT1").asLong());
				food.setCarbohydrate(node.get("NUTR_CONT2").asLong());
				food.setProtein(node.get("NUTR_CONT3").asLong());
				food.setFat(node.get("NUTR_CONT4").asLong());
				food.setNatrium(node.get("NUTR_CONT6").asLong());
				food.setCholesterol(node.get("NUTR_CONT7").asLong());
				foods.add(food);
			}
			foodsRepository.saveAll(foods);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
	@GetMapping("/foods/search/{foodName}")
	public List<Foods> search(@PathVariable String foodName) {
		return foodsService.search(foodName);
	}
	
//	@PostMapping("/dailyinsert")
//	public Foods insert(@RequestBody Foods foods) {
//		return foodsService.insert(foods);
//	}
	
	@GetMapping("/list")
	public List<Foods> list(){
		return foodsService.list();
	}
}
