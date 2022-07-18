package com.example.dailyhealth.ui.run;

import static com.example.dailyhealth.util.Constants.*;
import static com.example.dailyhealth.util.TrackingUtility.calculatePolyLineLength;

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
import com.example.dailyhealth.viewmodel.RunViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;



public class MapsFragment extends Fragment {


	ArrayList<ArrayList<LatLng>> mPathpoints = new ArrayList<>();
	Boolean mIsTracking;
 	float distanceInMeters = 0f;
	Long timeTosave = 0L;
	static GoogleMap gMap;
	RunViewModel model;

	public RunDAO runDAO;

	private OnMapReadyCallback callback = new OnMapReadyCallback() {

		@Override
		public void onMapReady(GoogleMap googleMap) {
			gMap = googleMap;
			addAllPolyLines();
		}
	};


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
							 @Nullable ViewGroup container,
							 @Nullable Bundle savedInstanceState) {
		model = new ViewModelProvider(this).get(RunViewModel.class);
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

		subscribeToObservers();

	}

	private void moveCameraToUser(){

		if(!mPathpoints.isEmpty() && (mPathpoints.get(mPathpoints.size()-1) != null)) {
			ArrayList<LatLng> PolyLine = mPathpoints.get(mPathpoints.size() - 1);
			int lenPointsofPolyLine = PolyLine.size();

			LatLng lastLatLng = PolyLine.get(lenPointsofPolyLine - 1);

			gMap.animateCamera(
					CameraUpdateFactory.newLatLngZoom(
							lastLatLng,
							MAP_ZOOM
					)
			);
		}
	}

	private void addLatestPolyLine(){

		// get latest 2 path point and draw polyline
		if (!mPathpoints.isEmpty() && (mPathpoints.get(mPathpoints.size() - 1).size() > 1)) {
			ArrayList<LatLng> PolyLine = mPathpoints.get(mPathpoints.size() - 1);
			int lenPointsofPolyLine = PolyLine.size();

			LatLng preLastLatLng = PolyLine.get(lenPointsofPolyLine - 2);
			LatLng lastLatLng = PolyLine.get(lenPointsofPolyLine - 1);

			PolylineOptions polylineOptions = new PolylineOptions()
					.color(POLYLINE_COLOR)
					.width(POLYLINE_WIDTH)
					.add(preLastLatLng) // 2번쨰 마지막 위치
					.add(lastLatLng); // 마지막 위치
			gMap.addPolyline(polylineOptions);
		}
	}

	private void addAllPolyLines(){
		for ( ArrayList<LatLng> polyline: mPathpoints) {
			PolylineOptions polylineOptions =
					new PolylineOptions()
							.color(POLYLINE_COLOR)
							.width(POLYLINE_WIDTH)
							.addAll(polyline);
			gMap.addPolyline(polylineOptions);
		}
	}

	private void subscribeToObservers(){
		TrackingService.isTracking.observe(getViewLifecycleOwner(), isTracking ->{
			mIsTracking = isTracking;
		});

		TrackingService.pathPoints.observe(getViewLifecycleOwner(), pathPoints ->{
			mPathpoints = pathPoints;
			moveCameraToUser();
			addLatestPolyLine();
			Log.d(TAG, "map fragment) pathPoint size " + pathPoints.size());
		});

		TrackingService.isFirstStart.observe(getViewLifecycleOwner(), isFirstStart -> {
			if( mPathpoints.size() > 0){
				zoomToSeeWholeTrack();
				for ( ArrayList<LatLng> polyline : mPathpoints ) {
					distanceInMeters += calculatePolyLineLength(polyline);

				}
				endRunAndSaveToDb();
			}
		});
//
//		TrackingService.trackingLocation.observe(getViewLifecycleOwner(), trackingLocation ->
//				gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(trackingLocation.getLatitude(), trackingLocation.getLongitude()), MAP_ZOOM))
//		);

		TrackingService.timeRunInMillis.observe(getViewLifecycleOwner(), timeRunInMillis->{
			timeTosave = timeRunInMillis;
		});
	}

	private void zoomToSeeWholeTrack(){
		LatLngBounds.Builder bounds = LatLngBounds.builder();
		for (ArrayList<LatLng> polyline: mPathpoints ) {
			for ( LatLng point : polyline ) {
				bounds.include(point);
			}
		}

		gMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 1));
	}

	private void endRunAndSaveToDb(){
		/**
		 * 날짜 변환
		 */
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
		SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.getDefault());
		SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.getDefault());

		String year = yearFormat.format(calendar.getTime());
		String month = monthFormat.format(calendar.getTime());
		String day = dayFormat.format(calendar.getTime());
		String title = ""+year+"년 "+month+"월 "+day+"일 러닝";

		gMap.snapshot( bmp -> {

			long timestamp = calendar.getTimeInMillis();
			Log.d(TAG, "달리기 기록이 저장되었습니다. " + timeTosave+ "// " + distanceInMeters);
			Run run =  new Run(bmp,timestamp, TrackingService.timeRunInMillis.getValue(),title, distanceInMeters);
			model.insertRun(run);
			Toast.makeText(getContext(), "달리기 기록이 저장되었습니다. " + timestamp + "// " + distanceInMeters, Toast.LENGTH_SHORT).show();

		});
	}




	
}