package com.example.kakaologinexample;

import com.example.kakaologinexample.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {

    //추가
    @POST("insert")
    Call<User> save(@Body User user);
}
