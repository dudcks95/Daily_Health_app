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
	
	Fragment mapsFragment = null;
	Fragment recordFragment = null;
	Button btnViewMap;
	private GoogleMap mMap;
	private UserFragment userFragment = new UserFragment();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		
		
		View fragmentRun = inflater.inflate(R.layout.fragment_run, container, false);
		btnViewMap = fragmentRun.findViewById(R.id.btnViewMap);
		
		btnViewMap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mapsFragment = new MapsFragment();
				Log.d(">>", "click");
				
				// Open fragment
				getActivity()
						.getSupportFragmentManager()
						.beginTransaction()
						.replace(R.id.container, mapsFragment)
						.commit();
			}
		});
		
		// Inflate the layout for this fragment
		
		return fragmentRun;
//		return inflater.inflate(R.layout.fragment_run, container, false);
		
		
	}
	
}