package com.example.dailyhealth.ui.cal;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodClient {
    private static Retrofit retrofit;

    static  Retrofit getClient(){
        retrofit = new Retrofit.Builder()
//                .baseUrl("http://192.168.219.103:1704/") // 집 IP
                .baseUrl("http://10.100.102.18:1704/")  // 학원 IP
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
