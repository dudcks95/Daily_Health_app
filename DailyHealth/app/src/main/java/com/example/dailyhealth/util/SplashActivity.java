package com.example.dailyhealth.util;

import android.content.Intent;
import android.os.Bundle;

import com.example.dailyhealth.MainActivity;
import com.example.dailyhealth.MainActivity2;

import androidx.appcompat.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            Thread.sleep(4000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        startActivity(new Intent(this, MainActivity.class));
        finish();

    }

}