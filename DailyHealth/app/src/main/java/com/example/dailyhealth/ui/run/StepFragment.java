package com.example.dailyhealth.ui.run;

import static com.example.dailyhealth.util.Constants.ACTION_START_OR_RESUME_SERVICE;
import static com.example.dailyhealth.util.Constants.ACTION_STOP_SERVICE;
import static com.example.dailyhealth.util.Constants.PREFERENCE_FILE_KEY;
import static com.example.dailyhealth.util.Constants.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dailyhealth.R;
import com.example.dailyhealth.service.StepService;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StepFragment extends Fragment {

    TextView stepCountView;
    Button btnStartStep, btnFinishStep;
    Boolean isTracking = false;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public StepFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static StepFragment newInstance(String param1, String param2) {
        StepFragment fragment = new StepFragment();
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
        Context context = getActivity();
        sharedPref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step, container, false);
        // 활동 퍼미션 체크
        if(ActivityCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED){

            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACTIVITY_RECOGNITION}, 0);
        }

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(10);
        stepCountView = view.findViewById(R.id.stepCountView);
        btnStartStep = view.findViewById(R.id.btnStartStep);
        btnFinishStep = view.findViewById(R.id.btnFinishStep);
        Log.d(TAG, "step frg " + isTracking);

        // 리셋 버튼 추가 - 리셋 기능
        btnStartStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    sendCommandToService(ACTION_START_OR_RESUME_SERVICE);
                    btnStartStep.setVisibility(View.GONE);
                    btnFinishStep.setVisibility(View.VISIBLE); // when start, you can finish running
            }
        });

        btnFinishStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommandToService(ACTION_STOP_SERVICE);
                btnStartStep.setVisibility(View.VISIBLE);
                btnFinishStep.setVisibility(View.GONE);
            }
        });

        StepService.mSteps.observe(getViewLifecycleOwner(), mSteps->{
            stepCountView.setText(mSteps.toString());
            progressBar.setProgress(mSteps);
            editor.putInt("pedometer", mSteps);
            editor.apply();
        });

        StepService.isTracking.observe(getViewLifecycleOwner(), isTracking ->{
            this.isTracking = isTracking;

            editor.putBoolean("isTracking", isTracking);
            editor.apply();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "step frg " + isTracking);
        if( this.isTracking ){
            btnStartStep.setVisibility(View.GONE);
            btnFinishStep.setVisibility(View.VISIBLE);
        }else{
            btnStartStep.setVisibility(View.VISIBLE);
            btnFinishStep.setVisibility(View.GONE);
        }
    }

    public void sendCommandToService(String action) {
        Intent intent = new Intent(requireContext(), StepService.class);
        intent.setAction(action);
        requireContext().startService(intent);
    }

}