package com.example.dailyhealth.util;

public final class Constants {

    public static final String TAG = ">>";
    /**
     * 서비스 상태(action) 상수
     */
    public static final String  ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE";
    public static final String  ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE";
    public static final String ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE";
    public static final String ACTION_SHOW_TRACKING_ACTIVITY = "ACTION_SHOW_TRACKING_ACTIVITY";

    /**
     * Notification 속성
     */
    public static final String NOTIFICATION_CHANNEL_ID = "tracking_channel";
    public static final String NOTIFICATION_CHANNEL_NAME = "Tracking";
    public static  final  String NOTIFICATION_CHANNEL_DESCRIPTION = "NOTIFICATION_CHANNEL_DESCRIPTION";
    public static final int NOTIFICATION_ID = 1; // 채널 ID는 0이면 안됨

    /*
    * shared Preference
    * */
    public static final String PREFERENCE_FILE_KEY = "com.example.dailyhealth.PREFERENCE_FILE_KEY";



}
