package com.example.dailyhealth.ui.run;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dailyhealth.R;
import com.example.dailyhealth.ui.user.UserFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class RunFragment extends Fragment {
	
	Fragment mapsFragment;
	Fragment recordFragment = new RecordFragment();
	Button btnViewMap, btnStartRecord;
	boolean flagBtnViewMap = false;
	FragmentManager fragmentManager;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		
		View fragmentRun = inflater.inflate(R.layout.fragment_run, container, false);
		btnViewMap = fragmentRun.findViewById(R.id.btnViewMap);
		btnStartRecord = fragmentRun.findViewById(R.id.btnStartRecord);
		
		
		fragmentManager = getChildFragmentManager();
		fragmentManager.beginTransaction().add(R.id.container, recordFragment).commit();
		
		btnStartRecord.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
			
				
			}
		});
		
		btnViewMap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				
				flagBtnViewMap = !(flagBtnViewMap);
				
				if (flagBtnViewMap == true) {
					
					//새로운 map를 가져와야함 갱신된 내용을 필요로 함
					mapsFragment = new MapsFragment();
					
					if (mapsFragment == null) {
						Toast.makeText(getActivity(), "지도를 불러올 수 없습니다.", Toast.LENGTH_SHORT).show();
					} else {
						// Replace fragment
						getChildFragmentManager()
								.beginTransaction()
								.replace(R.id.container, mapsFragment)
								// replace 다음에 적어준다.
								// replace시 fragment를 destroy하지 않고 backstack에 담아둔다.
								.addToBackStack(null)
								.commit();
					}
					
				} else {
					
					recordFragment = new RecordFragment();
					
					if (recordFragment == null) {
						Toast.makeText(getActivity(), "기록 화면으로 넘어갈 수 없습니다.", Toast.LENGTH_SHORT).show();
					} else {
						getChildFragmentManager()
								.beginTransaction()
								.replace(R.id.container, recordFragment)
								.addToBackStack(null)
								.commit();
					}
					
				}
				
				
			}
		});
		
		// Inflate the layout for this fragment
		
		return fragmentRun;
		
	}
	
	// findById 넣는 곳
	private void intitView() {
	
	}
}
