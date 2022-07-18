package com.example.dailyhealth.ui.cal;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FoodClient {
    private static Retrofit retrofit;

    static  Retrofit getClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://10.100.102.30:8704/")  // 학원 IP
                .addConverterFactory(GsonConverterFactory.create())
                .build();
//                .baseUrl("http://192.168.219.103:8704/") // 집 IP
//                .baseUrl("http://10.100.102.18:8704/")  // 학원 IP

        return retrofit;
    }
}
