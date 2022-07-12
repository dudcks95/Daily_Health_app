package com.example.dailyhealth.ui.run;

import static com.example.dailyhealth.util.Constants.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.dailyhealth.R;
import com.example.dailyhealth.db.Run;
import com.example.dailyhealth.db.RunDAO;
import com.example.dailyhealth.service.TrackingService;
import com.example.dailyhealth.ui.viewmodel.MainViewModel;
import com.example.dailyhealth.util.TrackingUtility;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MapsFragment extends Fragment {
	ArrayList<ArrayList<LatLng>> mPathpoints = new ArrayList<>();
	GoogleMap gMap;
	MainViewModel model;
	@Inject
	public RunDAO runDAO;


	private OnMapReadyCallback callback = new OnMapReadyCallback() {

		@Override
		public void onMapReady(GoogleMap googleMap) {
			gMap = googleMap;
			TrackingService.pathPoints.observe(getViewLifecycleOwner(),pathPoints -> {
				Log.d(TAG, "map fragment) pathPoint size " + pathPoints.size());
				mPathpoints = pathPoints;
				// 카메라를 마지막 점 위치로 이동
				if(!pathPoints.isEmpty() && (pathPoints.get(pathPoints.size()-1) != null)) {

					ArrayList<LatLng> PolyLine = pathPoints.get(pathPoints.size() - 1);
					int lenPointsofPolyLine = PolyLine.size();
					LatLng lastLatLng = PolyLine.get(lenPointsofPolyLine - 1);
					googleMap.animateCamera(
							CameraUpdateFactory.newLatLngZoom(
									lastLatLng,
									MAP_ZOOM
							)
					);

					// get latest 2 path point and draw polyline
					if (!pathPoints.isEmpty() && (PolyLine.size() > 2)) {
						LatLng preLastLatLng = PolyLine.get(lenPointsofPolyLine - 2);

						PolylineOptions polylineOptions = new PolylineOptions()
								.color(POLYLINE_COLOR)
								.width(POLYLINE_WIDTH)
								.add(preLastLatLng) // 2번쨰 마지막 위치
								.add(lastLatLng); // 마지막 위치
						googleMap.addPolyline(polylineOptions);
					}
				}

			});

			TrackingService.trackingLocation.observe(getViewLifecycleOwner(), trackingLocation ->
					googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(trackingLocation.getLatitude(), trackingLocation.getLongitude()), MAP_ZOOM))
			);

			TrackingService.isTracking.observe(getViewLifecycleOwner(), isTracking-> {
				if(!isTracking){
					Log.d(TAG, "map screen shot");
					zommToSeeWholeTrack();
					googleMap.snapshot(bitmap -> {
						int distanceInMeters = 0;
						Log.d(TAG,"pathpoints" + mPathpoints.size());
						for ( ArrayList<LatLng> polyline : mPathpoints ) {
							distanceInMeters = (int) TrackingUtility.calculatePolyLineLength(polyline);
						}
						Long dateTimestamp = Calendar.getInstance().getTimeInMillis();
						Run run = new Run(bitmap,dateTimestamp,distanceInMeters);
						Log.d(TAG, ""+ bitmap.toString() +"//"+dateTimestamp.toString()+"//"+distanceInMeters);
						Runnable r = new Runnable() {
							@Override
							public void run() {
								// 데이터에 읽고 쓸때는 쓰레드 사용
								runDAO.insertRun(run);
							}
						};
						Thread thread = new Thread(r);
						thread.start();
						Toast.makeText(getContext(),"save run", Toast.LENGTH_LONG).show();
					});
				}
			});
		}
	};

	private void zommToSeeWholeTrack(){
		LatLngBounds.Builder bounds = LatLngBounds.builder();
		for (ArrayList<LatLng> polyline: mPathpoints ) {
			for ( LatLng point : polyline ) {
				bounds.include(point);
			}
		}

		gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 1));
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		model = new ViewModelProvider(this).get(MainViewModel.class);
		return inflater.inflate(R.layout.fragment_maps, container, false);

	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		
		super.onViewCreated(view, savedInstanceState);
		SupportMapFragment mapFragment =
				(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
		if (mapFragment != null) {
			mapFragment.getMapAsync(callback);
		}
	}
	
}