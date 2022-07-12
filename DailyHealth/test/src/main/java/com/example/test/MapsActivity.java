package com.example.test;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.test.databinding.ActivityMapsBinding;
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

import android.util.Log;
import android.widget.Toast;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private static final int REQUEST_CODE = 101;
    Location currentLocation;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        if( googleMap != null){
            mMap = googleMap;

            LatLng currLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currLatLng).title("curr location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currLatLng));
        }else{
            Log.d(">>", "onMapReady에서 지도가 불러지지 않았습니다");
        }


    }


    private void getCurrentLocation() {
        // 권한 확인 알림창
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    Log.d(">>",""+ currentLocation.getAltitude());

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    // onMapReady 함수를 불러 googleMap을 불러온다.
                    mapFragment.getMapAsync(MapsActivity.this);
                }
            }
        });


        //Location 요청 설정
        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(60000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.d(">>",""+locationSettingsResponse.getLocationSettingsStates());
            }
        });
            /*LocationCallback mLocationCallback = new LocationCallback() {
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
            };*/


    }
}
