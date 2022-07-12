package com.example.dailyhealth.ui.run;

import static com.example.dailyhealth.util.Constants.ACTION_START_OR_RESUME_SERVICE;
import static com.example.dailyhealth.util.Constants.ACTION_STOP_SERVICE;
import static com.example.dailyhealth.util.Constants.REQUEST_CODE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.dailyhealth.R;
import com.example.dailyhealth.service.TrackingService;
import com.example.dailyhealth.util.Constants;
import com.example.dailyhealth.util.TrackingUtility;
import com.vmadalin.easypermissions.EasyPermissions;

public class TrackingFragment extends Fragment{
	
	Fragment mapsFragment = new MapsFragment();;
	Fragment recordFragment = new RecordFragment();
	Button btnViewMap, btnStartRecord;
	boolean flagBtnViewMap = false;
	boolean flagBtnStartRecord = false;
	FragmentManager fragmentManager;

	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	public TrackingFragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 *
	 * @return A new instance of fragment RecordFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static RecordFragment newInstance(String param1, String param2) {
		RecordFragment fragment = new RecordFragment();
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
		
		View fragmentRun = inflater.inflate(R.layout.fragment_tracking, container, false);
		btnViewMap = fragmentRun.findViewById(R.id.btnViewMap);
		btnStartRecord = fragmentRun.findViewById(R.id.btnStartRecord);
		
		fragmentManager = getChildFragmentManager();
		fragmentManager.beginTransaction().add(R.id.container, recordFragment).commit();

		requestPermissions();

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

	public void requestPermissions() {

		// 권한 확인 알림창
		if (TrackingUtility.hasLocationPermissions(requireContext())) { // 권한이 있는 경우에는 권한을 요청하지 않습니다.
			return;
		}

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
			EasyPermissions.requestPermissions(this,
					"이용을 위해 위치 권한을 '허용'으로 선택해주세요.",
					Constants.REQUEST_CODE_LOCATION_PERMISSION,
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_COARSE_LOCATION
			);
		} else {
			EasyPermissions.requestPermissions(
					this,
					"이용을 위해 위치 권한을 '허용'으로 선택해주세요.",
					Constants.REQUEST_CODE_LOCATION_PERMISSION,
					Manifest.permission.ACCESS_FINE_LOCATION,
					Manifest.permission.ACCESS_COARSE_LOCATION,
					Manifest.permission.ACCESS_BACKGROUND_LOCATION
			);
		}
}

}
