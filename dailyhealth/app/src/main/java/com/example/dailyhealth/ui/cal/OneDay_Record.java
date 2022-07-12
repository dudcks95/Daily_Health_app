package com.example.dailyhealth.ui.cal;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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

        ActivityResultLauncher<Intent> launcher;

        Intent intent = getIntent();

        int month = intent.getIntExtra("month",0);
        String day = intent.getStringExtra("day");
        txDay.setText(month +"월 " + day+"일");


        launcher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult data) {
                        if(data.getResultCode() == Activity.RESULT_OK){
                            Intent intent = data.getData();
                            //int result = intent.getIntExtra("result",0);

                        }
                    }
                });

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
                intent1.putExtra("month", month);
                intent1.putExtra("day", day);
//                startActivity(intent1);
                launcher.launch(intent1);
            }
        });
    }
}