package com.example.dailyhealth.service;

import static com.example.dailyhealth.util.Constants.ACTION_SHOW_TRACKING_ACTIVITY;
import static com.example.dailyhealth.util.Constants.ACTION_START_OR_RESUME_SERVICE;
import static com.example.dailyhealth.util.Constants.ACTION_STOP_SERVICE;
import static com.example.dailyhealth.util.Constants.NOTIFICATION_CHANNEL_DESCRIPTION;
import static com.example.dailyhealth.util.Constants.NOTIFICATION_CHANNEL_ID;
import static com.example.dailyhealth.util.Constants.NOTIFICATION_CHANNEL_NAME;
import static com.example.dailyhealth.util.Constants.NOTIFICATION_ID;
import static com.example.dailyhealth.util.Constants.PREFERENCE_FILE_KEY;
import static com.example.dailyhealth.util.Constants.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleService;
import androidx.lifecycle.MutableLiveData;

import com.example.dailyhealth.MainActivity;
import com.example.dailyhealth.R;

public class StepService extends LifecycleService implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepCountSensor;
    //현재 걸음 수
    public static MutableLiveData<Integer> mSteps = new MutableLiveData<>();
    //리스너가 등록되고 난 후의 step count
    public static MutableLiveData<Integer> mCounterSteps = new MutableLiveData<>();

    public static MutableLiveData<Boolean> isTracking = new MutableLiveData<>();
    private  boolean isFirstStart = false; // 처음 실행 여부
//    Context mContext = getApplicationContext();

    // 현재 걸음수 초기화
    public  void postInitialValues(){
        SharedPreferences sharedPref = this.getApplicationContext().getSharedPreferences(PREFERENCE_FILE_KEY,Context.MODE_PRIVATE);
        int pedometer = sharedPref.getInt("pedometer", 0);
        boolean sharedIdTracking = sharedPref.getBoolean("isTracking", false);
        if( sharedIdTracking ){
            mSteps.setValue(pedometer);
            isTracking.setValue(sharedIdTracking);
        }
        else{
            mSteps.setValue(0);
            isTracking.setValue(false);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(@NonNull Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //센서 연결[걸음수 센서를 이용한 흔듬 감지]
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        stepCountSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Log.d(TAG,"service created");
        if(stepCountSensor == null){
            Toast.makeText(getApplicationContext(),"No Step Detect Sensor",Toast.LENGTH_SHORT).show();
        }else{
            //센서의 속도 설정
            sensorManager.registerListener(this,stepCountSensor,SensorManager.SENSOR_DELAY_FASTEST);
        }

        postInitialValues();
        Log.d(TAG,"service created " + mSteps.getValue() + "??");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        switch (intent.getAction()) {
            case ACTION_START_OR_RESUME_SERVICE:
                if (!isFirstStart) {
                    Log.d(TAG, "서비스 시작함");
                    isFirstStart = false;
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

        return super.onStartCommand(intent, flags, startId);
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

        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(ACTION_SHOW_TRACKING_ACTIVITY );
        return PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

    }

    private void stopService() {
        Log.d(TAG,"stopService ");
        stopForeground(true);
        if(sensorManager!=null){
            sensorManager.unregisterListener(this);
        }
        postInitialValues();
        stopSelf();
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "Service Stopped");
        // 유대폰이 켜지고 난 후 걸음수.
        // 12시가 되면 리셋
        // 휴태폰이 꺼지면 하루동안 걸음 수 를 저장.
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_STEP_COUNTER){
            mSteps.postValue(mSteps.getValue()+1);
            Log.i(TAG, "New step detected by STEP_COUNTER sensor. Total step count: " + mSteps +"//"+ (int) event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
