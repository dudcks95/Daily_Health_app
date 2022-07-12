package com.example.dailyhealth.ui.cal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface FoodService {
    //음식 검색
    @GET("/foods/search/{foodName}")
    Call<List<Foods>> searchFood(@Path("foodName") String foodName);
}
