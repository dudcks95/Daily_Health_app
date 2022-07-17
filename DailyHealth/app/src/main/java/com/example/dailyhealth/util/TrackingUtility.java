package com.example.dailyhealth.util;

import static com.example.dailyhealth.util.Constants.TAG;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.util.Log;


import com.google.android.gms.maps.model.LatLng;
import com.vmadalin.easypermissions.EasyPermissions;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class TrackingUtility {
    // 위치 권한 확인
    public static Boolean hasLocationPermissions(Context context){
        Log.d(TAG,"hasLocationPermissions "+ context.toString());

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            return EasyPermissions.hasPermissions(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            );

        } else {
            return EasyPermissions.hasPermissions(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            );

        }


    }


    public static float calculatePolyLineLength(ArrayList<LatLng> polyLine){
        float distance = 0f;
        for (int i = 0; i < polyLine.size()-2 ; i++) {
            LatLng pos1 = polyLine.get(i);
            LatLng pos2 = polyLine.get(i+1);
            float[] result = new float[1];

            Location.distanceBetween(pos1.latitude
                    , pos1.longitude
                    , pos2.latitude
                    ,pos2.longitude
                    ,result);
            distance += result[0];
        }

        return distance;
    }

    public static String getFormattedStopWatch(Long ms, Boolean includeMillis){
        Long milliseconds = ms;
        Long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
        milliseconds -= TimeUnit.HOURS.toMillis(hours);
        Long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
        milliseconds -= TimeUnit.MINUTES.toMillis(minutes);
        Long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);

        if (!includeMillis) {
            return (hours < (long)10 ? "0" : "") + hours + ':' + (minutes < (long)10 ? "0" : "") + minutes + ':' + (seconds < (long)10 ? "0" : "") + seconds;
        } else {
            milliseconds -= TimeUnit.SECONDS.toMillis(seconds);
            milliseconds /= (long)10;
            return (hours < (long)10 ? "0" : "") + hours + ':' + (minutes < (long)10 ? "0" : "") + minutes + ':' + (seconds < (long)10 ? "0" : "") + seconds + ':' + (milliseconds < (long)10 ? "0" : "") + milliseconds;
        }
    }
}
