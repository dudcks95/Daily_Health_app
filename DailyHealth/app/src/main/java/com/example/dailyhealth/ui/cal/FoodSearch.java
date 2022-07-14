package com.example.dailyhealth.ui.cal;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.dailyhealth.R;
import com.example.dailyhealth.model.Foods;
import com.example.dailyhealth.service.FoodService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodSearch extends AppCompatActivity {
    private FoodService foodService;
    Foods_Recycler_Adapter fAdapter;
    LinearLayoutManager manager;

    ImageButton btnBackOneDay;
    EditText edtFoodName;
    Button btnFoodSearch;

    RecyclerView foodList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);

        btnBackOneDay = findViewById(R.id.btnBackOneDay);
        edtFoodName = findViewById(R.id.edtFoodName);
        btnFoodSearch = findViewById(R.id.btnFoodSearch);

        foodList = findViewById(R.id.foodList);

        ActivityResultLauncher<Intent> launcher;
        Intent intent = getIntent();

        int month = intent.getIntExtra("month", 0);
        String day = intent.getStringExtra("day");


        btnBackOneDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnFoodSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                foodService = FoodClient.getClient().create(FoodService.class);
                Call<List<Foods>> call = foodService.searchFood(edtFoodName.getText().toString());

                call.enqueue(new Callback<List<Foods>>() {
                    @Override
                    public void onResponse(Call<List<Foods>> call, Response<List<Foods>> response) {
                        List<Foods> foodsList = response.body();
                        manager = new LinearLayoutManager(FoodSearch.this,
                                RecyclerView.VERTICAL, false);
                        foodList.setLayoutManager(manager);
                        fAdapter = new Foods_Recycler_Adapter(foodsList, month, day);
                        foodList.setAdapter(fAdapter);
                        fAdapter.notifyDataSetChanged();
                        Log.d("foodList>>",foodList.toString());

                    }

                    @Override
                    public void onFailure(Call<List<Foods>> call, Throwable t) {
                        Log.d("foodList x>>>", foodList+"");
                    }
                });
            }
        });


    }
}