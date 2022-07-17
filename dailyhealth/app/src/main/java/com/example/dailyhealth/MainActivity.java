package com.example.dailyhealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.dailyhealth.databinding.ActivityMainBinding;
import com.example.dailyhealth.model.User;
import com.example.dailyhealth.service.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/*

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


}*/

public class MainActivity extends AppCompatActivity  {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navigateToTrackingFragmentIfNeeded(getIntent());
        BottomNavigationView navView = findViewById(R.id.btm_Navibar);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.userFragment, R.id.calFragment, R.id.stepFragment)
                .build();
        //Navigation Controller 객체를 이용해야 합니다. Controller는 Host에 보여지고 있는 View를 변경해주는 역할을 맡고 있습니다.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.btmNavibar, navController);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        navigateToTrackingFragmentIfNeeded(intent);
    }

    private void navigateToTrackingFragmentIfNeeded(Intent intent){
        if(intent.getAction() == "ACTION_SHOW_TRACKING_ACTIVITY") {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_global_stepFragment);
        }
    }
}