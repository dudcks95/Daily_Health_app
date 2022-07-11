package com.example.dailyhealth.ui.cal;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyhealth.R;

import java.util.List;

import retrofit2.Call;

public class Foods_Recycler_Adapter extends RecyclerView.Adapter<Foods_Recycler_Adapter.viewHolder>{
    private List<Foods> foodList;
    private FoodService foodService;

//    private List<>

    public Foods_Recycler_Adapter (List<Foods> foodList){
        this.foodList = foodList;
    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_food, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Foods foods = foodList.get(position);
        holder.txFoodName.setText(foods.getFoodName());
        holder.txFoodKcal.setText(foods.getKcal().toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View dialogView= view.inflate(view.getContext(), R.layout.layout_food_dialog, null);
                TextView txName = dialogView.findViewById(R.id.txName);
                TextView txKcal = dialogView.findViewById(R.id.txKcal);
                TextView txCarb = dialogView.findViewById(R.id.txCarb);
                TextView txProtein = dialogView.findViewById(R.id.txProtein);
                TextView txFat = dialogView.findViewById(R.id.txFat);

                txName.setText(foods.getFoodName());
                txKcal.setText(foods.getKcal().toString());
                txCarb.setText(foods.getCarbohydrate().toString());
                txProtein.setText(foods.getClass().toString());
                txFat.setText(foods.getFat().toString());

                AlertDialog.Builder dlg = new AlertDialog.Builder(view.getContext());
                dlg.setTitle("음식 정보(1회 기준량)");
                dlg.setView(dialogView);
                dlg.setPositiveButton("등록", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Foods foodsdto = new Foods(
                                txName.getText().toString(),
                                Long.parseLong(txKcal.getText().toString()),
                                Long.parseLong(txCarb.getText().toString()),
                                Long.parseLong(txProtein.getText().toString()),
                                Long.parseLong(txFat.getText().toString())
                        );
                        FoodService foodService = FoodClient.getClient().create(FoodService.class);

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
