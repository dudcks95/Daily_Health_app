package com.example.dailyhealth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;

import com.example.dailyhealth.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
                R.id.userFragment, R.id.calFragment, R.id.runFragment)
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
        if(intent.getAction() == "ACTION_SHOW_TRACKING_FRAGMENT") {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
            navController.navigate(R.id.action_global_trackingFragment);
        }
    }
}