package com.example.dailyhealth.ui.run;

import static com.example.dailyhealth.util.Constants.ACTION_PAUSE_SERVICE;
import static com.example.dailyhealth.util.Constants.ACTION_START_OR_RESUME_SERVICE;
import static com.example.dailyhealth.util.Constants.ACTION_STOP_SERVICE;
import static com.example.dailyhealth.util.Constants.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dailyhealth.R;
import com.example.dailyhealth.service.TrackingService;
import com.example.dailyhealth.util.TrackingUtility;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.dialog.MaterialDialogs;


public class TrackingFragment extends Fragment {

    MaterialButton btnToggleRun, btnFinishRun;
    ImageButton btnCancleTracking;
    TextView tvTimer;
    Boolean isTracking = false; // 실행중에 있는가 ?
    Boolean isFirstStart = false; // 시작 안했거나 실행중
    private Long currentTimeInMillis = 0L;
    OnBackPressedCallback callback;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Fragment mapsFragment = new MapsFragment();
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tracking, container, false);
        btnToggleRun = view.findViewById(R.id.btnToggleRun);
        btnFinishRun = view.findViewById(R.id.btnFinishRun);
        btnCancleTracking = view.findViewById(R.id.btnCancleTracking);
        tvTimer = view.findViewById(R.id.tvTimer);

        // Replace fragment
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.mapContainer, mapsFragment)
                // replace 다음에 적어준다.
                // replace시 fragment를 destroy하지 않고 backstack에 담아둔다.
                .addToBackStack(null)
                .commit();

        subscribeToObservers();
        requestPermissions();
        getActivity().getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
               cancelTracking();
            }
        });


        btnToggleRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleRun();
            }
        });

        btnFinishRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCommandToService(ACTION_STOP_SERVICE);
                btnToggleRun.setText("걷기 시작");
                btnFinishRun.setVisibility(View.GONE);
            }
        });

        btnCancleTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelTracking();
            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");
        subscribeToObservers();
    }

    private void cancelTracking() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext(), androidx.appcompat.R.style.AlertDialog_AppCompat_Light);
        builder.setTitle("경로 기록을 취소하시겠습니까?")
                .setPositiveButton("네", (dialogInterface, i) -> {
                    sendCommandToService(ACTION_STOP_SERVICE);
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main);
                    navController.navigate(R.id.runFragment);
                })
                .setNegativeButton("아니요", (dialogInterface, i) -> {
                })
                .create();
        builder.show();
    }


    private void updateTracking(Boolean isTracking){
        this.isTracking = isTracking;
        if( isFirstStart ){
            if( isTracking ){
                // on tracking
                btnToggleRun.setText("정지");
                btnFinishRun.setVisibility(View.GONE);

            }else {
                // not tracking == pause the run
                btnToggleRun.setText("다시 시작");
                btnFinishRun.setVisibility(View.VISIBLE);
            }
        }else{
            btnToggleRun.setText("시작");
            btnFinishRun.setVisibility(View.GONE);
        }

    }

    private void toggleRun(){
        if(this.isTracking){
            sendCommandToService(ACTION_PAUSE_SERVICE);
        }else{
            sendCommandToService(ACTION_START_OR_RESUME_SERVICE);
        }
    }

    private void subscribeToObservers(){
        TrackingService.isTracking.observe(getViewLifecycleOwner(), isTracking ->{
            updateTracking(isTracking);
        });

        TrackingService.timeRunInMillis.observe(getViewLifecycleOwner(), timeRunInMillis ->{
            currentTimeInMillis = timeRunInMillis;
            String formattedTime = TrackingUtility.getFormattedStopWatch(currentTimeInMillis, true);
            tvTimer.setText(formattedTime);
        });

        TrackingService.isFirstStart.observe(getViewLifecycleOwner(), isFirstRun ->{
            this.isFirstStart = isFirstRun;
            Log.e(TAG, isFirstRun + "firstRun");
        });

    }

    public void sendCommandToService(String action) {
        Intent intent = new Intent(requireContext(), TrackingService.class);
        intent.setAction(action);
        requireContext().startService(intent);
    }


    public void requestPermissions() {
        // 권한 확인 알림창
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if( callback !=  null)
            callback.remove();
    }
}