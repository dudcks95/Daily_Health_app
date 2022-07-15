package com.example.dailyhealth.ui.cal;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dailyhealth.R;
import com.example.dailyhealth.model.FoodsRecord;
import com.example.dailyhealth.service.FoodRecordService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OneDay_Record extends AppCompatActivity {
    ImageButton btnBack;
    TextView txDay, txMorningMenu, txMorningKcal, txLunchMenu, txLunchKcal, txDinnerMenu, txDinnerKcal;
    Button btnFood, btnTrain;
    int monththis;
    String daythis;
    Long sum = 0L;

    private FoodRecordService foodRecordService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_day_record);

        btnBack = findViewById(R.id.btnBack);
        txDay = findViewById(R.id.txDay);
        btnFood = findViewById(R.id.btnFood);
        btnTrain = findViewById(R.id.btnTrain);

        txMorningMenu = findViewById(R.id.txMorningMenu);
        txMorningKcal = findViewById(R.id.txMorningKcal);
        txLunchMenu = findViewById(R.id.txLunchMenu);
        txLunchKcal = findViewById(R.id.txLunchKcal);
        txDinnerMenu = findViewById(R.id.txDinnerMenu);
        txDinnerKcal = findViewById(R.id.txDinnerKcal);

        ActivityResultLauncher<Intent> launcher;

        Intent intent = getIntent();

        int month = intent.getIntExtra("month",0);
        String day = intent.getStringExtra("day");

        monththis = month;
        daythis = day;

        Long morning, lunch, dinner;


        txDay.setText(month +"월 " + day+"일");



        launcher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult data) {
                        if(data.getResultCode() == Activity.RESULT_OK){
                            Intent intent = data.getData();
                            //int result = intent.getIntExtra("result",0);
                            //FoodsRecord result = (FoodsRecord) intent.getSerializableExtra("result");
                        }
                    }
                });

        foodRecordService = FoodClient.getClient().create(FoodRecordService.class);
        Call<List<FoodsRecord>> callmorning = foodRecordService.selectfoodsRecord(1L, monththis, daythis);
        callmorning.enqueue(new Callback<List<FoodsRecord>>() {
            @Override
            public void onResponse(Call<List<FoodsRecord>> call, Response<List<FoodsRecord>> response) {
                List<FoodsRecord> foodsList = response.body();
                Log.d("sizemorning>>",foodsList.size()+"");

                if(foodsList != null && foodsList.size() > 0){
                    Log.d("sizemorning>>",foodsList.size()+"");
                    Log.d("foodsList>>>",foodsList.get(1).getFoodName());
                    Log.d("eatTIme>>",foodsList.get(1).getEatTime()+"");
                    txMorningMenu.setText(foodsList.get(0).getFoodName());
                    txMorningKcal.setText(foodsList.get(0).getKcal().toString().trim());
                    txLunchMenu.setText(foodsList.get(1).getFoodName());
                    txLunchKcal.setText(foodsList.get(1).getKcal().toString().trim());
                    txDinnerMenu.setText(foodsList.get(2).getFoodName());
                    txDinnerKcal.setText(foodsList.get(2).getKcal().toString().trim());
//                    sum += Long.parseLong(txMorningKcal.getText().toString());
//                    Intent intent = new Intent();
//                    intent.putExtra("sum", sum);
                }else{
                    Log.d("result>>>" , "null입니다.");
                }
            }

            @Override
            public void onFailure(Call<List<FoodsRecord>> call, Throwable t) {
                Log.d("result>>>" , "null입니다.");
            }
        });

//        Call<List<Foods>> callLunch = foodRecordService.selectfoodsRecord(1L, monththis, daythis, 2L);
//        callLunch.enqueue(new Callback<List<Foods>>() {
//            @Override
//            public void onResponse(Call<List<Foods>> call, Response<List<Foods>> response) {
//                List<Foods> foodsList = response.body();
////                Log.d("sizelunch>>",foodsList.size()+"");
//                if(foodsList != null && foodsList.size() > 0){
//                    txLunchMenu.setText(foodsList.get(0).getFoodName());
//                    txLunchKcal.setText(foodsList.get(0).getKcal().toString().trim());
//                    sum += Long.parseLong(txLunchKcal.getText().toString());
//                    Intent intent = new Intent();
//                    intent.putExtra("sum", sum);
//                }else{
//                    Log.d("result>>>" , "null입니다.");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Foods>> call, Throwable t) {
//
//            }
//        });
//
//        Call<List<Foods>> callDinner = foodRecordService.selectfoodsRecord(1L, monththis, daythis, 3L);
//        callDinner.enqueue(new Callback<List<Foods>>() {
//            @Override
//            public void onResponse(Call<List<Foods>> call, Response<List<Foods>> response) {
//                List<Foods> foodsList = response.body();
////                Log.d("response>>",foodsList+"");
////                Log.d("sizedinner>>",foodsList.size()+"");
//                if(foodsList != null && foodsList.size() > 0){
//                    txDinnerMenu.setText(foodsList.get(0).getFoodName());
//                    txDinnerKcal.setText(foodsList.get(0).getKcal().toString().trim());
//                    sum += Long.parseLong(txDinnerKcal.getText().toString());
//                    Intent intent = new Intent();
//                    intent.putExtra("sum", sum);
//                }else{
//                    Log.d("result>>>" , "null입니다.");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<Foods>> call, Throwable t) {
//
//            }
//        });




        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(OneDay_Record.this, CalFragment.class);
//                intent.putExtra("sum", sum);
//                Log.d("sum>>", sum+"");
//                startActivity(intent);

                finish();
            }
        });

        btnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(OneDay_Record.this, FoodSearch.class);
                intent1.putExtra("month", month);
                intent1.putExtra("day", day);
                startActivity(intent1);
//                launcher.launch(intent1);
            }
        });

        btnTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //View dialogView =
            }
        });


    }
    public Long getSum(){
        return sum;
    }
}