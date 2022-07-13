package com.example.dailyhealth.ui.cal;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FoodRecordService {

    @POST("/foodinsert")
    Call<FoodsRecord> insert(@Body FoodsRecord foodsRecord);

    @GET("/selectfoodsRecord/{userid}/{month}/{day}/{eatTime}")
    Call<List<Foods>> selectfoodsRecord(@Path("userid") Long userid, @Path("month") int month,
                                        @Path("day") String day, @Path("eatTime") Long eatTime);
}
