package com.example.dailyhealth.service;

import com.example.dailyhealth.model.Foods;
import com.example.dailyhealth.model.FoodsRecord;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FoodRecordService {

    @POST("/foodinsert")
    Call<FoodsRecord> insert(@Body FoodsRecord foodsRecord);


//    @GET("/selectfoodsRecord/{userid}/{month}/{day}/{eatTime}")
//    Call<List<Foods>> selectfoodsRecord(@Path("userid") Long userid, @Path("month") int month,
//                                        @Path("day") String day, @Path("eatTime") Long eatTime);

    @GET("/selectfoodsRecord/{userid}/{month}/{day}")
    Call<List<FoodsRecord>> selectfoodsRecord(@Path("userid") Long userid, @Path("month") int month,
                                        @Path("day") String day);

    @GET("/sumkcal/{userid}/{month}/{day}")
    Call<Long> sumkcal(@Path("userid") Long userid, @Path("month") int month,
                       @Path("day") String day);

    @GET("/sumkcal/{userid}/{month}")
    Call<List<FoodsRecord>> sumkcal2(@Path("userid") Long userid, @Path("month") int month);
}
