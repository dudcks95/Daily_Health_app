package com.example.dailyhealth.ui.cal;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FoodRecordService {

    @POST("/foodinsert")
    Call<FoodsRecord> insert(@Body FoodsRecord foodsRecord);
}
