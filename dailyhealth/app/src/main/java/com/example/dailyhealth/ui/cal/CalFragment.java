package com.example.dailyhealth.ui.cal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dailyhealth.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalFragment extends Fragment {


    ImageButton prevBtn, nextBtn;
    View view;
    RecyclerView calendarView;
    TextView timeTextView;
    GridLayoutManager layoutManager;
    //저장된 날짜
    private TextView[] StorageCalendar = new TextView[42];

    //현재 날짜
    GregorianCalendar cal;

    ActivityResultLauncher<Intent> launcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_cal, container, false);
        prevBtn = (ImageButton) view.findViewById(R.id.prevBtn);
        nextBtn = (ImageButton) view.findViewById(R.id.nextBtn);
        prevBtn.setOnClickListener(imageBtnClickEvent);
        nextBtn.setOnClickListener(imageBtnClickEvent);
        timeTextView = (TextView) view.findViewById(R.id.timeTextView);

        launcher= registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult data) {
                        if(data.getResultCode() == Activity.RESULT_OK){
                            Intent intent = data.getData();
                            FoodsRecord result = (FoodsRecord) intent.getSerializableExtra("foodsRecord");

                        }
                    }
                });

        for(int i=0; i<StorageCalendar.length; i++){
            int pos = i;
            StorageCalendar[i]=new TextView(view.getContext());
            StorageCalendar[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    Log.d("ID>>>>" , StorageCalendar[pos].getText().toString()+"");
                }
            });
        }


        //현재 날짜
        cal = new GregorianCalendar();
        CalendarSetting(cal);

        RecyclerViewCreate();
        //Log.d("YYY>>>>",StorageCalendar[6].getText().toString());



        return view;



    }
    View.OnClickListener imageBtnClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.prevBtn:
                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)-1, 1);
                    Log.d("--------년도>>>>",cal.get(Calendar.YEAR)+"");
                    Log.d("월>>>>",cal.get(Calendar.MONTH)+"");
                    break;
                case R.id.nextBtn:
                    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1,1);
                    Log.d("------------년도>>>>",cal.get(Calendar.YEAR)+"");
                    Log.d("월>>>>",cal.get(Calendar.MONTH)+"");
                    break;
            }
            CalendarSetting(cal);

            timeTextView.setText(cal.get(Calendar.YEAR) + "년" + (cal.get(Calendar.MONTH)+1)+"월");
        }
    };

    //RecyclerView 생성
    public void RecyclerViewCreate(){
        //Recycler Calendar
        calendarView = view.findViewById(R.id.calendar);
        Calendar_Recycler_Adapter calendarAdapter = new Calendar_Recycler_Adapter(view.getContext(),StorageCalendar,launcher, cal);

        layoutManager = new GridLayoutManager(view.getContext(),7);
        calendarView.setLayoutManager(layoutManager);
        calendarView.setAdapter(calendarAdapter);

    }

    // 캘린더 날짜 데이터 세팅
    public void CalendarSetting(GregorianCalendar cal){
        // 현재 날짜의 1일
        GregorianCalendar calendar = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                1, 0, 0, 0
        );


        // 저번달의 첫번째 1일
//        GregorianCalendar prevCalendar = new GregorianCalendar(
//                cal.get(Calendar.YEAR),
//                cal.get(Calendar.MONTH)-1,
//                1, 0, 0, 0
//        );

        // 특정 월에 시작하는 요일-1 해서 빈칸 구하기
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
        Log.d("dayOfWeek >>>>>",dayOfWeek+"");

        // 한달의 최대일 그 이후의 빈공간 만들기
        // 해당 월의 마지막 요일
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)-1;
        Log.d("max >>>>>",max+"");

        for(int i = 0; i<StorageCalendar.length; i++){
            if(i < dayOfWeek) { // 저번달의 끝의 일수를 설정
                //StorageCalendar[i] = Integer.toString(prevCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)- dayOfWeek + i +1);
                //StorageCalendar[i] = "";
                StorageCalendar[i].setText("");
            } else if (i > (max + dayOfWeek)) { // 이번 달의 끝 이후의 일수를 설정
//                StorageCalendar[i] = Integer.toString(i - (max+dayOfWeek));
//                StorageCalendar[i].setText(Integer.toString(i - (max+dayOfWeek)));
                StorageCalendar[i].setText("");
            } else { // 이번달 일수
//                StorageCalendar[i] = " " + (i - dayOfWeek+1) + " ";
                StorageCalendar[i].setText(" " + (i - dayOfWeek+1) + " ");
            }
        }
        RecyclerViewCreate();
    }



}