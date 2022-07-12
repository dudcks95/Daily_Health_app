package com.example.dailyhealth.ui.run;

import static com.example.dailyhealth.util.Constants.ACTION_PAUSE_SERVICE;
import static com.example.dailyhealth.util.Constants.ACTION_START_OR_RESUME_SERVICE;
import static com.example.dailyhealth.util.Constants.ACTION_STOP_SERVICE;
import static com.example.dailyhealth.util.Constants.MAP_ZOOM;
import static com.example.dailyhealth.util.Constants.POLYLINE_COLOR;
import static com.example.dailyhealth.util.Constants.POLYLINE_WIDTH;
import static com.example.dailyhealth.util.Constants.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.example.dailyhealth.R;
import com.example.dailyhealth.service.TrackingService;
import com.example.dailyhealth.ui.viewmodel.MainViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class TrackingFragment2 extends Fragment {

    MaterialButton btnToggleRun, btnFinishRun;
    Chronometer tvTimer;

    @NonNull
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Fragment mapsFragment = new MapsFragment();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tracking2, container, false);
        btnToggleRun = view.findViewById(R.id.btnToggleRun);
        btnFinishRun = view.findViewById(R.id.btnFinishRun);
        tvTimer = view.findViewById(R.id.tvTimer);

        // Replace fragment
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.mapContainer, mapsFragment)
                // replace 다음에 적어준다.
                // replace시 fragment를 destroy하지 않고 backstack에 담아둔다.
                .addToBackStack(null)
                .commit();

        btnToggleRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* flagBtnStartRecord = !flagBtnStartRecord;
                Log.d(TAG, btnToggleRun.getText().toString());
                if(flagBtnStartRecord){
                    sendCommandToService(ACTION_START_OR_RESUME_SERVICE);
                    btnToggleRun.setText("STOP");
                    btnFinishRun.setVisibility(View.GONE);
                    tvTimer.setBase(SystemClock.elapsedRealtime());
                    tvTimer.start();
                } else{
                    sendCommandToService(ACTION_STOP_SERVICE);
                    btnToggleRun.setText("START");
                    btnFinishRun.setVisibility(View.VISIBLE);
                    tvTimer.stop();
                }*/
                sendCommandToService(ACTION_START_OR_RESUME_SERVICE);
                tvTimer.setBase(SystemClock.elapsedRealtime());
                tvTimer.start();
                btnToggleRun.setVisibility(View.GONE);
                btnFinishRun.setVisibility(View.VISIBLE); // when start, you can finish running
            }
        });

        btnFinishRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvTimer.stop();
                sendCommandToService(ACTION_STOP_SERVICE);
                btnToggleRun.setVisibility(View.VISIBLE);
                btnFinishRun.setVisibility(View.GONE);
            }
        });

        return view;

    }

    public void sendCommandToService(String action) {
        Intent intent = new Intent(requireContext(), TrackingService.class);
        intent.setAction(action);
        requireContext().startService(intent);
    }

    private void endRunAndSaveToDb(){

    }


}