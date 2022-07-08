package com.example.dailyhealth.util;

import static com.example.dailyhealth.util.Constants.TAG;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.vmadalin.easypermissions.EasyPermissions;

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

/*    public void requestPermissions(Context context) {
        if (TrackingUtility.hasLocationPermissions(context)) { // 권한이 있는 경우에는 권한을 요청하지 않습니다.
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(this
                    ,
                    "이용을 위해 위치 권한을 '허용'으로 선택해주세요.",
                    Constants.REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            );
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "이용을 위해 위치 권한을 '허용'으로 선택해주세요.",
                    Constants.REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            );
        }
    }*/
}
