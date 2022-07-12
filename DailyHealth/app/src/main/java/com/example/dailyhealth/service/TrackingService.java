package com.example.dailyhealth.service;

import static com.example.dailyhealth.util.Constants.*;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.example.dailyhealth.R;
import com.example.dailyhealth.ui.run.RunFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;

import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class TrackingService extends LifecycleService {

	private FusedLocationProviderClient fusedLocationProviderClient;    // 위치 정보 제공자 클라이언트
	private LocationCallback locationCallback;

	ArrayList<LatLng> PolyLine = new ArrayList<>(); // 두 점을 이은 선
	ArrayList<ArrayList<LatLng>> PolyLines = new ArrayList<>(); // 선들을 모은 것
	// pathPoints => MutableLiveData<PolyLines>()
	public static MutableLiveData<ArrayList<ArrayList<LatLng>>> pathPoints = new MutableLiveData<>();
	public static MutableLiveData<Location> trackingLocation = new MutableLiveData<>(); // 현재 위피 추적

	public static MutableLiveData<Boolean> isTracking = new MutableLiveData<>();
	private  boolean isFirstStart = false; // 처음 실행 여부

	// 시간
	public static MutableLiveData<Long> timeRunInMillis = new MutableLiveData<>(); // 뷰에 표시될 시간
	private static MutableLiveData<Long> timeRunInSeconds = new MutableLiveData<Long>(); // 알림창에 표시될 시간

	private Boolean isTimerEnabled = false; // 타이머 실행 여부
	private Long lapTime = 0L; // 시작 후 측정한 시간
	private Long totalTime = 0L; // 정지 시 저장되는 시간
	private Long timeStarted = 0L; // 측정 시작된 시간
	private Long lastSecondTimestamp = 0L; // 1초 단위 체크를 위함

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		super.onBind(intent);
		return null;
	}

	private void postInitialValues() {
		isTracking.postValue(false);
		PolyLines = new ArrayList<>();
		Log.d(TAG,PolyLines.size() +"/ postInitialValues /"+ PolyLine.toString());
		pathPoints.postValue(PolyLines);
		timeRunInSeconds.postValue(0L);
		timeRunInMillis.postValue(0L);
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "location onCreate()");
		super.onCreate();

		postInitialValues();

		//위치 서비스 클라이언트 생성
		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

		isTracking.observe(this, new Observer<Boolean>() {
			@Override
			public void onChanged(Boolean aBoolean) {
				Log.d(TAG, "isTracking observe isTracking : " + aBoolean);
				updateLocation(aBoolean);
			}
		});

		locationCallback = new LocationCallback() {
			@Override
			public void onLocationResult(@NonNull LocationResult locationResult) {
				super.onLocationResult(locationResult);
				if( isTracking.getValue() ){
					if (locationResult == null) {
						Log.d(TAG,"null");
						return;
					}
					for (Location location : locationResult.getLocations()) {
						Log.d(TAG,"add");
						addPathPoint(location);
						trackingLocation.setValue(location);
					}
				}
			}
		};
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		super.onStartCommand(intent, flags, startId);
		switch (intent.getAction()) {
			case ACTION_START_OR_RESUME_SERVICE:
				if (!isFirstStart) {
					Log.d(TAG, "서비스 시작함");
					isFirstStart = false;
					isTracking.postValue(true);
					startForegroundService();
				} else {
					Log.d(TAG, "서비스 실행중");
				}
				break;
			case ACTION_STOP_SERVICE:
				Log.d(TAG, "서비스 종료");
				isTracking.postValue(false);
				stopService();
				break;
		}
		return START_STICKY;
	}

	public void startForegroundService() {

		isTracking.postValue(true);
		// 알림 채널 생성
		createNotificationChannel();

		// 알림 내용 설정
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
				.setAutoCancel(false)
				.setOngoing(true)
				.setSmallIcon(R.drawable.run)
				.setContentTitle("Daily Health")
				.setContentText("running service is on")
				.setPriority(NotificationCompat.PRIORITY_DEFAULT)
	 			.setContentIntent(getMainActivityPendingIntent());

		// 알림 시작
		startForeground(NOTIFICATION_ID, builder.build());
	}

	// 알림 채널 만들기 및 중요도 설정
	private void createNotificationChannel() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

			CharSequence name = NOTIFICATION_CHANNEL_NAME;
			String description = NOTIFICATION_CHANNEL_DESCRIPTION;
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);

			NotificationManager notificationManager = getSystemService(NotificationManager.class);
			channel.setDescription(description);
			notificationManager.createNotificationChannel(channel);
		}
	}

	//알림창 선택시 이동 설정정
	 private PendingIntent getMainActivityPendingIntent(){

		Intent intent = new Intent(this, RunFragment.class);
		intent.setAction(ACTION_SHOW_TRACKING_ACTIVITY );
		return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

	}

	@SuppressLint("MissingPermission")
	private void updateLocation(Boolean isTracking) {
		Log.d(TAG,"updateLocation) isTracking : " + isTracking);
		if(isTracking){
			//Location 요청 설정
			LocationRequest locationRequest = LocationRequest.create();
			locationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
			locationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
			locationRequest.setPriority(Priority.PRIORITY_HIGH_ACCURACY);

			LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
					.addLocationRequest(locationRequest);

			// 요청 설정 확인
			SettingsClient client = LocationServices.getSettingsClient(this);
			Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


			//요청이 성공이라면 위치정보를 받아온다.
			task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
				@Override
				public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
					Log.d(TAG,"updateLocation) 위치 요청 결과 "+ locationSettingsResponse.getLocationSettingsStates());
					fusedLocationProviderClient.requestLocationUpdates(locationRequest , locationCallback, Looper.getMainLooper());				}
			});
		}else {
			Log.d(TAG,"updateLocation) removeLocationUpdates");
			fusedLocationProviderClient.removeLocationUpdates(locationCallback);
		}

	}

	public void addPathPoint(@Nullable Location location){

		Log.d(TAG,"add Point LatLng ) "+location.getLatitude() +" / "+ location.getLongitude());
		LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
		PolyLine.add(point);
		PolyLines.add(PolyLine);
		pathPoints.setValue(PolyLines);
		Log.d(TAG,"add Point PolyLines size) " + PolyLines.size());
	}


	private void stopService() {
		Log.d(TAG,"stopService ) "+ isTracking.getValue());
		stopForeground(true);
		stopSelf();
		postInitialValues();
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
