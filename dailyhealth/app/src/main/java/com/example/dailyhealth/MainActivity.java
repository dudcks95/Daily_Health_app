package com.example.dailyhealth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.dailyhealth.ui.cal.CalFragment;
import com.example.dailyhealth.ui.msg.MsgFragment;
import com.example.dailyhealth.ui.run.RunFragment;
import com.example.dailyhealth.ui.user.UserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity  {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private CalFragment calFragment = new CalFragment();
    private MsgFragment msgFragment = new MsgFragment();
    private RunFragment runFragment = new RunFragment();
    private UserFragment userFragment = new UserFragment();

    LinearLayout home_ly;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        home_ly = findViewById(R.id.home_ly);
        bottomNavigationView = findViewById(R.id.btm_Navibar);



        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.home_ly, userFragment).commitAllowingStateLoss();

        //맨 처음 시작 탭
        bottomNavigationView.setSelectedItemId(R.id.home_ly);

        //선택 리스너
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                switch (item.getItemId())
                {
                    case R.id.tabUser :
                        transaction.replace(R.id.home_ly, userFragment).commitAllowingStateLoss();
                        break;
                    case R.id.tabCal :
                        transaction.replace(R.id.home_ly, calFragment).commitAllowingStateLoss();
                        break;
                    case R.id.tabMsg :
                        transaction.replace(R.id.home_ly, msgFragment).commitAllowingStateLoss();
                        break;
                    case R.id.tabRun :
                        transaction.replace(R.id.home_ly, runFragment).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });
    }

    MutableLiveData<ArrayList<Object>> mCalendarList = new MutableLiveData<>();


}