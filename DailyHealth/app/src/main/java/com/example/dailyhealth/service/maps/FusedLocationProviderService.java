package com.example.dailyhealth.service.maps;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.dailyhealth.MainActivity;
import com.example.dailyhealth.ui.run.RunFragment;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class FusedLocationProviderService extends Service {
	
	private static final String TAG = FusedLocationProviderService.class.getSimpleName();
	
	private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
	private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
	private static final int REQUEST_CHECK_SETTINGS = 100;
	
	private LocationRequest locationRequest;
	private FusedLocationProviderClient fusedLocationProviderClient;    // 위치 정보 제공자 클라이언트
	private LocationCallback locationCallback; // 위치 변화시 callback
	private Location location; // 현재 위치
	private LocationSettingsRequest locationSettingsRequest;
	private Context context;
	private SettingsClient settingsClient;
	
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		Log.d(">>", "location onCreate()");
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		buildGoogleApiClient();
		return START_STICKY;
	}
	
	protected synchronized void buildGoogleApiClient() {
		
		locationRequest = LocationRequest.create();
		locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
		locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
		locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);
		
		LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
				.addLocationRequest(locationRequest);
		
		Task<LocationSettingsResponse> result =
				LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
		
		result.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
			@Override
			public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
				fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
				if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					// TODO: Consider calling
					//    ActivityCompat#requestPermissions
					// here to request the missing permissions, and then overriding
					//   public void onRequestPermissionsResult(int requestCode, String[] permissions,
					//                                          int[] grantResults)
					// to handle the case where the user grants the permission. See the documentation
					// for ActivityCompat#requestPermissions for more details.
					return;
				}
				Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
				
				Log.d(TAG, "" + locationTask.getResult());
			}
		});
	}
	
	
	@Override
	public void onDestroy() {
		Log.e(TAG, "Service Stopped");
		
		if (fusedLocationProviderClient != null) {
			Log.e(TAG, "Location Update Callback Removed");
		}
		super.onDestroy();
	}
	
	
}
