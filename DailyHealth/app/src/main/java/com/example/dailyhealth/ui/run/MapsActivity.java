package com.example.dailyhealth.ui.run;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.dailyhealth.R;
import com.example.dailyhealth.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;

import android.widget.Toast;

import androidx.core.app.ActivityCompat;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
	
	private static final int REQUEST_CODE = 101;
	Location currentLocation;
	private ActivityMapsBinding binding;
	private GoogleMap mMap;
	private FusedLocationProviderClient fusedLocationProviderClient;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		binding = ActivityMapsBinding.inflate(getLayoutInflater());
		setContentView(binding.getRoot());
		
		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
//		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//				.findFragmentById(R.id.map);
//		mapFragment.getMapAsync(MapsActivity.this);
		getCurrentLocation();
		
		
	}
	
	@Override
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		
		LatLng currLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
		mMap.addMarker(new MarkerOptions().position(currLatLng).title("curr location"));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(currLatLng));
		
	}
	
	private void getCurrentLocation() {
		// 권한 확인
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED
				&& ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
				!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
			return;
		}
		
		
		LocationRequest mLocationRequest = LocationRequest.create();
		mLocationRequest.setInterval(60000);
		mLocationRequest.setFastestInterval(5000);
		mLocationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
		LocationCallback mLocationCallback = new LocationCallback() {
			@Override
			public void onLocationResult(LocationResult locationResult) {
				Toast.makeText(getApplicationContext(), " location result is  " + locationResult, Toast.LENGTH_LONG).show();
				
				if (locationResult == null) {
					Toast.makeText(getApplicationContext(), "current location is null ", Toast.LENGTH_LONG).show();
					
					return;
				}
				for (Location location : locationResult.getLocations()) {
					if (location != null) {
						Toast.makeText(getApplicationContext(), "current location is " + location.getLongitude(), Toast.LENGTH_LONG).show();
						
						//TODO: UI updates.
					}
				}
			}
		};
		
		Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
		locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
			@Override
			public void onSuccess(Location location) {
				if (location != null) {
					currentLocation = location;
					Toast.makeText(getApplicationContext(), "" + currentLocation.getAltitude(), Toast.LENGTH_LONG).show();
					SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
							.findFragmentById(R.id.map);
					mapFragment.getMapAsync(MapsActivity.this);
				}
			}
		});
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (REQUEST_CODE) {
			case REQUEST_CODE:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					getCurrentLocation();
				}
				break;
		}
	}
}