package com.example.dailyhealth.ui.run;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.dailyhealth.R;
// origin
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepFragment2 extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    TextView stepCountView;
    Button btnToggleRun;
    //현재 걸음 수
    private int mSteps = 0;
    //리스너가 등록되고 난 후의 step count
    private int mCounterSteps = 0;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StepFragment2() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static StepFragment2 newInstance(String param1, String param2) {
        StepFragment2 fragment = new StepFragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_step, container, false);

        stepCountView = view.findViewById(R.id.stepCountView);
        btnToggleRun = view.findViewById(R.id.btnStartStep);

        btnToggleRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 현재 걸음수 초기화
                mSteps = 0;
                mCounterSteps = 0;
                stepCountView.setText(Integer.toString(mSteps));

            }
        });

        return view;
    }

    public void onStart() {
        super.onStart();
        if(stepCountSensor !=null){
            //센서의 속도 설정
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_FASTEST);
        }
    }


    public void onStop(){
        super.onStop();
        if(sensorManager!=null){
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){

            //stepcountsenersor는 앱이 꺼지더라도 초기화 되지않는다. 그러므로 우리는 초기값을 가지고 있어야한다.
            if (mCounterSteps < 1) {
                // initial value
                mCounterSteps = (int) event.values[0];
            }
//            mSteps = (int) event.values[0] - mCounterSteps;
            mSteps++;
            stepCountView.setText(Integer.toString(mSteps));
            Log.i("log: ", "New step detected by STEP_COUNTER sensor. Total step count: " + mSteps +"//"+ (int) event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}