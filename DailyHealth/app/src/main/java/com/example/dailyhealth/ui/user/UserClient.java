package com.example.dailyhealth.ui.user;

import com.example.dailyhealth.service.UserService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserClient {
    private static UserClient instance;
    private UserService userService;


    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build();

    public UserClient(){
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.100.102.30:8704/")
                .baseUrl("http://192.168.0.2:8704/")  // 학원 IP
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        userService = retrofit.create(UserService.class);
    }

    public static UserClient getInstance() {
        if(instance == null) {
            instance = new UserClient();
        }
        return instance;
    }

    public UserService getUserService() {
        return userService;
    }
}
