package com.example.dailyhealth.service;

import com.example.dailyhealth.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {

    //추가
    @POST("insert")
    Call<User> save(@Body User user);


    //내 리스트
    @GET("list/{email}")
    Call<User> list(@Path("email") String email);

    //수정
    @PUT("update/{email}")
    Call<User> update(@Path("email") String email, @Body User user);

    //삭제
    @DELETE("delete/{email}")
    Call<Void> delete(@Path("email") String email);
}
