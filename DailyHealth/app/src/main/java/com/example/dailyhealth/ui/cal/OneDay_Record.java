package com.example.dailyhealth.ui.cal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dailyhealth.R;


public class OneDay_Record extends AppCompatActivity {
    ImageButton btnBack;
    TextView txDay;
    Button btnFood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_day_record);

        btnBack = findViewById(R.id.btnBack);
        txDay = findViewById(R.id.txDay);
        btnFood = findViewById(R.id.btnFood);
        Intent intent = getIntent();

        int month = intent.getIntExtra("month",0);
        String day = intent.getStringExtra("day");
        txDay.setText(month +"월 " + day+"일");



        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(OneDay_Record.this, FoodSearch.class);
                startActivity(intent1);
            }
        });
    }
}