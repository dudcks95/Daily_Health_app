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
	
	Fragment mapsFragment;
	Fragment recordFragment = new RecordFragment();
	Button btnViewMap;
	boolean flagBtnViewMap = false;
	Fragment user = new UserFragment();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		
		
		View fragmentRun = inflater.inflate(R.layout.fragment_run, container, false);
		btnViewMap = fragmentRun.findViewById(R.id.btnViewMap);
		
		btnViewMap.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if( flagBtnViewMap == true){

					//새로운 map를 가져와야함 갱신된 내용을 필요로 함
					mapsFragment = new MapsFragment();

					// Replace fragment
					getChildFragmentManager()
							.beginTransaction()
							.replace(R.id.container, mapsFragment)
							// replace 다음에 적어준다.
							// replace시 fragment를 destroy하지 않고 backstack에 담아둔다.
							.addToBackStack(null)
							.commit();
				}else{
					getChildFragmentManager()
							.beginTransaction()
							.replace(R.id.container, recordFragment)
							.addToBackStack(null)
							.commit();
				}

				flagBtnViewMap = !(flagBtnViewMap);
			}
		});

		// Inflate the layout for this fragment

		return fragmentRun;
//		return inflater.inflate(R.layout.fragment_run, container, false);


	}
	
	// findById 넣는 곳
	private void intitView(){

	}
}
