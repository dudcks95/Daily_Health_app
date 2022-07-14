package com.example.dailyhealth.service;

import com.example.dailyhealth.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    //추가
    @POST("insert")
    Call<User> save(@Body User user);
}
