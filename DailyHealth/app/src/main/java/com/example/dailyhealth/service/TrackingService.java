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

import com.example.dailyhealth.MainActivity;
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
import androidx.lifecycle.LifecycleCoroutineScope;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleOwnerKt;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;

public class TrackingService extends LifecycleService {

	private FusedLocationProviderClient fusedLocationProviderClient;    // 위치 정보 제공자 클라이언트
	private LocationCallback locationCallback;

	ArrayList<LatLng> PolyLine = new ArrayList<>(); // 두 점을 이은 선
	ArrayList<ArrayList<LatLng>> PolyLines = new ArrayList<>(); // 선들을 모은 것
	// pathPoints => MutableLiveData<PolyLines>()
	public static MutableLiveData<ArrayList<ArrayList<LatLng>>> pathPoints = new MutableLiveData<>();
	public static MutableLiveData<Location> trackingLocation = new MutableLiveData<>(); // 현재 위피 추적

	public static MutableLiveData<Boolean> isTracking = new MutableLiveData<>();
	public static MutableLiveData<Boolean> isFirstStart = new MutableLiveData<>(false); // 처음 실행 여부

	// 시간
	public static MutableLiveData<Long> timeRunInMillis = new MutableLiveData<>(0L); // 뷰에 표시될 시간
	private static MutableLiveData<Long> timeRunInSeconds = new MutableLiveData<Long>(0L); // 알림창에 표시될 시간

	private Long lapTime = 0L; // 정지 -> 시작, 시작 -< 정지 후 측정한 시간
	private Long timeRun = 0L; // total time
	private Long timeStarted = 0L; // when we started timer
	private Long lastSecondTimestamp = 0L; // 1초 단위 체크를 위함

	Thread threadTimer;

	private void startTimer(){
		addEmptyPolyLine();

		isTracking.setValue(true);

		threadTimer = new Thread() {
			@Override
			public void run() {
				timeStarted = System.currentTimeMillis();
				// 위치 추적 상태일 때
				while (isTracking.getValue()){
					// 현재 시간 - 시작 시간 => 경과한 시간
					lapTime = System.currentTimeMillis() - timeStarted;
					// 총시간 (일시정지시 저장된 시간) + 경과시간 전달
					timeRunInMillis.postValue(timeRun + lapTime);
					// 알림창에 표시될 시간 초 단위로 계산함
					if(timeRunInMillis.getValue() >= lastSecondTimestamp + 1000L){
						timeRunInSeconds.postValue(timeRunInSeconds.getValue() + 1);
						lastSecondTimestamp += 1000L;
					}
					try {
						Thread.sleep(TIMER_UPDATE_INTERVAL);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				// 위치 추적이 종료(정지) 되었을 때 총시간 저장
				timeRun += lapTime;;
			}
		};

		threadTimer.start();

	}

	private void postInitialValues() {
		isTracking.setValue(false);
		PolyLine = new ArrayList<>();
		pathPoints.setValue(PolyLines);
		timeRunInMillis.setValue(0L);
		timeRunInSeconds.setValue(0L);
		Log.d(TAG,"POSTINITIAL VALUES - PolyLine : "+ PolyLine.toString() + " , isTracking : " + isTracking.getValue() +" , pathPoint : " + pathPoints.getValue());

	}

	private void addEmptyPolyLine(){

		if( pathPoints.getValue() == null ){
			PolyLines = new ArrayList<>();
		}

		pathPoints.setValue(PolyLines);
		Log.d(TAG,"ADD EMPTY POLYLINES : "+ PolyLine.toString() + " , pathPoint : " + pathPoints.getValue());
	}


	public void startForegroundService() {
		startTimer();
		isTracking.postValue(true);
		Log.d(TAG, "startForground Service : "+ isTracking.getValue());
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

		Intent intent = new Intent(this, MainActivity.class);
		intent.setAction(ACTION_SHOW_TRACKING_FRAGMENT);
		return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

	}

	@SuppressLint("MissingPermission")
	private void updateLocation(Boolean isTracking) {
		Log.d(TAG,"UPDATE LOCATION - isTracking : " + isTracking);
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
					Log.d(TAG,"UPDATE LOCATION - REQUEST Location "+ locationSettingsResponse.getLocationSettingsStates());
					fusedLocationProviderClient.requestLocationUpdates(locationRequest , locationCallback, Looper.getMainLooper());				}
			});
		}else {
			Log.d(TAG,"UPDATE LOCATION - REMOVE Location ");
			fusedLocationProviderClient.removeLocationUpdates(locationCallback);
		}

	}

	public void addPathPoint(@Nullable Location location){

		Log.d(TAG,"ADD - Location "+location.getLatitude() +" : "+ location.getLongitude());
		LatLng point = new LatLng(location.getLatitude(), location.getLongitude());
		PolyLine.add(point);
		PolyLines.add(PolyLine);
		pathPoints.setValue(PolyLines);
		Log.d(TAG,"ADD - POLYLINE SIZE : " + PolyLines.size());
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		super.onBind(intent);
		return null;
	}


	@Override
	public void onCreate() {
		Log.e(TAG, "OnCreate()");
		super.onCreate();

		postInitialValues();

		//위치 서비스 클라이언트 생성
		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

		isTracking.observe(this, new Observer<Boolean>() {
			@Override
			public void onChanged(Boolean aBoolean) {
				Log.e(TAG, "OBSERVING ISTRACKING : " + aBoolean);
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
				if (!isFirstStart.getValue()) {
					Log.d(TAG, "서비스 시작함");
					isFirstStart.setValue(true);
					startForegroundService();
				} else {
					Log.d(TAG, "서비스 실행중");
					startTimer();
				}
				break;
			case ACTION_PAUSE_SERVICE:
				Log.d(TAG, "서비스 정지");
				pauseService();
				break;
			case ACTION_STOP_SERVICE:
				Log.d(TAG, "서비스 종료");
				isTracking.setValue(false);
				stopService();
				break;
		}

		return START_STICKY;
	}

	private void pauseService() {
		isTracking.postValue(false);
	}

	private void stopService() {
		Log.d(TAG,"STOP SERVICE -  "+ isTracking.getValue());
		stopForeground(true);
		stopSelf();
		postInitialValues();
		isFirstStart.postValue(false);
		timeRunInMillis.postValue(timeRun + lapTime);

	}

	@Override
	public void onDestroy() {
		Log.e(TAG, "Service DESTROY");
		if (fusedLocationProviderClient != null) {
			Log.e(TAG, "Location Update Callback Removed");
		}
		super.onDestroy();
	}


}
