package com.example.dailyhealth;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserClient {
    private static UserClient instance;
    private UserService userService;

    public UserClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.100.102.24:8704/")
                .addConverterFactory(GsonConverterFactory.create())
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
