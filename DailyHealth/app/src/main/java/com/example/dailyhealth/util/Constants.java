package com.example.dailyhealth.util;

import android.graphics.Color;

public final class Constants {

    public static final String TAG = ">>";
    /**
     * 서비스 상태(action) 상수
     */
    public static final String  ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE";
    public static final String  ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE";
    public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
    public static final String ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT";
    public static final String ACTION_SHOW_TRACKING_ACTIVITY = "ACTION_SHOW_TRACKING_ACTIVITY";
    /**
     * Tracking 옵션
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000L; //5s
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 2000L; //2s
    public static final int REQUEST_CODE = 100;

    /**
     * Notification 속성
     */
    public static final String NOTIFICATION_CHANNEL_ID = "tracking_channel";
    public static final String NOTIFICATION_CHANNEL_NAME = "Tracking";
    public static  final  String NOTIFICATION_CHANNEL_DESCRIPTION = "NOTIFICATION_CHANNEL_DESCRIPTION";
    public static final int NOTIFICATION_ID = 1; // 채널 ID는 0이면 안됨


    /**
     * 경로 표시 옵션
     */
    public static final float POLYLINE_WIDTH = 10f;
    public static final float MAP_ZOOM = 15f;
    public static final int  POLYLINE_COLOR = Color.RED;

    // 타이머 갱신 주기
    public static final long TIMER_UPDATE_INTERVAL = 50L;
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 0;

    /*
    * DB
    * */
    public static final String RUN_DB_NAME = "RunDB";

    /*
     * shared Preference
     * */
    public static final String PREFERENCE_FILE_KEY = "com.example.dailyhealth.PREFERENCE_FILE_KEY";
}
