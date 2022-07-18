package com.example.dailyhealth.ui.cal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ComponentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyhealth.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import com.example.dailyhealth.service.FoodRecordService;
import com.example.dailyhealth.ui.cal.OneDay_Record;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Calendar_Recycler_Adapter extends RecyclerView.Adapter<Calendar_Recycler_Adapter.viewHolder>  {

    Context context; // MainActivity
    TextView[] data; // 일자
    GregorianCalendar cal; // 날짜
    ActivityResultLauncher<Intent> launcher;
    private FoodRecordService foodRecordService;


    public Calendar_Recycler_Adapter(Context context, TextView[] data, ActivityResultLauncher<Intent> launcher, GregorianCalendar cal){
        super();
        this.context = context;
        this.data = data;
        this.launcher = launcher;
        this.cal = cal;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recycler_calendar, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.dayTextView.setText(data[position].getText()); //  일자

        foodRecordService = FoodClient.getClient().create(FoodRecordService.class);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("버튼ID >>>>>",data[position].getText().toString()+"");
                Intent intent = new Intent(context, OneDay_Record.class);
                intent.putExtra("month", cal.get(Calendar.MONTH)+1);
                String st[] = data[position].getText().toString().split(" ");
                intent.putExtra("day", st[0]);
//                intent.putExtra("day", dayNum+"");
                launcher.launch(intent);
                holder.itemView.setBackgroundColor(Color.BLUE);
            }
        });



    }

    @Override
    public int getItemCount() {
        return data.length;
    }


    public class viewHolder extends RecyclerView.ViewHolder{
        TextView dayTextView;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            dayTextView = itemView.findViewById(R.id.recycler_day);
        }
    }

}
