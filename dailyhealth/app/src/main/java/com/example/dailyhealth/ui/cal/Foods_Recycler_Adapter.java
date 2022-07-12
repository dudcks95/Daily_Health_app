package com.example.dailyhealth.ui.cal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyhealth.R;
import com.example.dailyhealth.ui.user.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Foods_Recycler_Adapter extends RecyclerView.Adapter<Foods_Recycler_Adapter.viewHolder>{
    private List<Foods> foodList;
    //private FoodService foodService;
    private FoodRecordService foodRecordService;
    private List<FoodsRecord> foodsRecordList;
    private Long rdobtnResult;
    private User user;
    Foods foods;
    int month;
    String day;
    Context context;

    public Foods_Recycler_Adapter (List<Foods> foodList, int month, String day){
        this.foodList = foodList;
        this.month = month;
        this.day = day;
    }

//    public void addItem(FoodsRecord foodsRecord){
//        foodsRecordList.add(foodsRecord);
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_food, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        foods = foodList.get(position);
        holder.txFoodName.setText(foods.getFoodName());
        holder.txFoodKcal.setText(foods.getKcal().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("foodid>>>",foods.getFoodId()+"");
                View dialogView= view.inflate(view.getContext(), R.layout.layout_food_dialog, null);
                TextView txName = dialogView.findViewById(R.id.txName);
                TextView txKcal = dialogView.findViewById(R.id.txKcal);
                TextView txCarb = dialogView.findViewById(R.id.txCarb);
                TextView txProtein = dialogView.findViewById(R.id.txProtein);
                TextView txFat = dialogView.findViewById(R.id.txFat);
                RadioGroup rdoGroup = dialogView.findViewById(R.id.rdoGroup);

                txName.setText(foods.getFoodName());
                txKcal.setText(foods.getKcal().toString());
                txCarb.setText(foods.getCarbohydrate().toString());
                txProtein.setText(foods.getProtein().toString());
                txFat.setText(foods.getFat().toString());


                AlertDialog.Builder dlg = new AlertDialog.Builder(view.getContext());
                dlg.setTitle("음식 정보(1회 기준량)");
                dlg.setView(dialogView);
                dlg.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("foodid>>>",foods.getFoodId()+"");
                        RadioGroup.OnCheckedChangeListener radioGroupBtn = new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                                if (i == R.id.rdoBreakfast){
                                    rdobtnResult = 1L;
                                } else if(i == R.id.rdoLunch){
                                    rdobtnResult = 2L;
                                } else if(i == R.id.rdoDinner){
                                    rdobtnResult = 3L;
                                }
                            }
                        };
                        Log.d("foodtime >>>",rdobtnResult+"");
                        Long foodid = foods.getFoodId();
                        FoodsRecord foodsRecordDto = new FoodsRecord(
                                foods.getFoodId(),
                                month,
                                day,
                                1L,
                                //user.getUserid()
                                1L
                        );
//                        foodsRecordDto.setFoodId(foods.getFoodId());
//                        foodsRecordDto.set
                        foodRecordService = FoodClient.getClient().create(FoodRecordService.class);
                        Call<FoodsRecord> call = foodRecordService.insert(foodsRecordDto);
                        call.enqueue(new Callback<FoodsRecord>() {
                            @Override
                            public void onResponse(Call<FoodsRecord> call, Response<FoodsRecord> response) {
//                                addItem(response.body());
                                Intent intent = new Intent();
                                //intent.putExtra("foodsRecord",foods);

                            }

                            @Override
                            public void onFailure(Call<FoodsRecord> call, Throwable t) {

                            }
                        });

                    }
                });
                dlg.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList==null?0:foodList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView txFoodName, txFoodKcal;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            this.txFoodName = itemView.findViewById(R.id.txFoodName);
            this.txFoodKcal = itemView.findViewById(R.id.txFoodKcal);
        }
    }
}
