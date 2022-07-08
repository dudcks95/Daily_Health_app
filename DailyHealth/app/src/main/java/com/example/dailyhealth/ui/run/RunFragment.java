package com.example.dailyhealth.ui.run;

import android.content.Intent;
import android.os.Bundle;
import static com.example.dailyhealth.util.Constants.*;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.dailyhealth.R;
import com.example.dailyhealth.service.maps.TrackingService;
import com.example.dailyhealth.util.TrackingUtility;

public class RunFragment extends Fragment {
	
	Fragment mapsFragment;
	Fragment recordFragment = new RecordFragment();
	Button btnViewMap, btnStartRecord;
	boolean flagBtnViewMap = false;
	boolean flagBtnStartRecord = false;
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
				flagBtnStartRecord = !flagBtnStartRecord;

				if(flagBtnStartRecord == true){
					sendCommandToService(ACTION_START_OR_RESUME_SERVICE);
				} else{
					sendCommandToService(ACTION_STOP_SERVICE);
				}

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

	public void sendCommandToService(String action) {
		Intent intent = new Intent(requireContext(), TrackingService.class);
		intent.setAction(action);
		requireContext().startService(intent);
	}
}
