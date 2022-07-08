package com.example.dailyhealth.ui.run;

import static com.example.dailyhealth.util.Constants.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dailyhealth.R;
import com.example.dailyhealth.service.maps.TrackingService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MapsFragment extends Fragment {

	ArrayList<LatLng> PolyLine = new ArrayList<>(); // 두 점을 이은 선
	ArrayList<ArrayList<LatLng>> PolyLines = new ArrayList<>(); // 선들을 모은 것
	ArrayList<Polyline> pathPoints = new ArrayList<>();
	private GoogleMap googleMap;

	private OnMapReadyCallback callback = new OnMapReadyCallback() {
		
		/**
		 * Manipulates the map once available.
		 * This callback is triggered when the map is ready to be used.
		 * This is where we can add markers or lines, add listeners or move the camera.
		 * In this case, we just add a marker near Sydney, Australia.
		 * If Google Play services is not installed on the device, the user will be prompted to
		 * install it inside the SupportMapFragment. This method will only be triggered once the
		 * user has installed Google Play services and returned to the app.
		 */

		@Override
		public void onMapReady(GoogleMap googleMap) {
//			gMap = googleMap;
			LatLng sydney = new LatLng(-50, 148);
			googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
			TrackingService.pathPoints.observe(getViewLifecycleOwner(),pathPoints -> {
//				moveCameraToUser();
//				addLatestPolyline();
				if(!pathPoints.isEmpty() && (pathPoints.get(pathPoints.size()-1) != null)) {
					Log.d(TAG, "map fragment " + pathPoints.size());
					ArrayList<LatLng> PolyLine = pathPoints.get(pathPoints.size() - 1);
//				Polyline polyline = pathPoints.get(pathPoints.size()-1);
					LatLng lastLatLng = PolyLine.get(PolyLine.size() - 1);
					googleMap.animateCamera(
							CameraUpdateFactory.newLatLngZoom(
									lastLatLng,
									MAP_ZOOM
							)
					);

					if (!pathPoints.isEmpty() && (PolyLine.size() > 2)) {
//					Polyline polyline = pathPoints.get(pathPoints.size()-1);
						int lenPointsofPolyLine = PolyLine.size();
						Log.d(TAG, "lenlen" + lenPointsofPolyLine);
						// 2번쨰 마지막 위치
						LatLng preLastLatLng = PolyLine.get(lenPointsofPolyLine - 2);
						// 마지막 위치

//					LatLng lastLatLng = PolyLine.get(lenPointsofPolyLine-1);
						PolylineOptions polylineOptions = new PolylineOptions()
								.color(POLYLINE_COLOR)
								.width(POLYLINE_WIDTH)
								.add(preLastLatLng)
								.add(lastLatLng);
						googleMap.addPolyline(polylineOptions);
					}
				}
			});

			TrackingService.trackingLocation.observe(getViewLifecycleOwner(), trackingLocation ->
					googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(trackingLocation.getLatitude(), trackingLocation.getLongitude()), MAP_ZOOM))
			);
		}
	};
	private void subscribeToObservers() {


	}

	private void moveCameraToUser() {
		if(!pathPoints.isEmpty() && (pathPoints.get(pathPoints.size()-1) != null)) {
			// 마지막 위치 point로 카메라 이동
			Polyline polyline = pathPoints.get(pathPoints.size());
			LatLng lastLatLng = polyline.getPoints().get(polyline.getPoints().size());
			googleMap.animateCamera(
					CameraUpdateFactory.newLatLngZoom(
							lastLatLng,
							MAP_ZOOM
					)
			);
		}
	}

	// get latest 2 path point and draw polyline
	private void addLatestPolyline() {

		if(!pathPoints.isEmpty() && (pathPoints.get(pathPoints.size()-1).getPoints().size() > 2)) {
			Polyline polyline = pathPoints.get(pathPoints.size()-1);
			int lenPointsofPolyLine = polyline.getPoints().size();
			Log.d(TAG, "lenlen"+lenPointsofPolyLine);
			// 2번쨰 마지막 위치
			LatLng preLastLatLng = polyline.getPoints().get(lenPointsofPolyLine-2);
			// 마지막 위치

			LatLng lastLatLng = polyline.getPoints().get(lenPointsofPolyLine-1);
			PolylineOptions polylineOptions =  new PolylineOptions()
													.color(POLYLINE_COLOR)
													.width(POLYLINE_WIDTH)
													.add(preLastLatLng)
													.add(lastLatLng);
			googleMap.addPolyline(polylineOptions);
		}
	}
	
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
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