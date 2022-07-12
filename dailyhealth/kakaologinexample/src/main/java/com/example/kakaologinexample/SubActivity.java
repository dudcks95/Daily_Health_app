package com.example.kakaologinexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;

import java.util.Arrays;
import java.util.List;


public class SubActivity extends AppCompatActivity {
    private String strNick, strProfileImg, strEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);


        Intent intent = getIntent();
        strNick = intent.getStringExtra("name");
        strProfileImg = intent.getStringExtra("profileImg");
        strEmail = intent.getStringExtra("email");
        //strGender = intent.getStringExtra("gender");
        String bundle = intent.getExtras().get("gender").toString();

        TextView tv_nick = findViewById(R.id.tv_nickName);
        TextView tv_email = findViewById(R.id.tv_email);
        ImageView iv_profile = findViewById(R.id.iv_profile);
        TextView tv_gender = findViewById(R.id.tv_gender);
//        Log.d(TAG,"invoke: 5555551111="+bundle);
        // 닉네임 set
        tv_nick.setText(strNick);
        // 이메일 set
        tv_email.setText(strEmail);
        // 프로필 이미지 사진 set
        Glide.with(this).load(strProfileImg).into(iv_profile);
        //성별 set
        tv_gender.setText(bundle);

        //로그아웃
        findViewById(R.id.btn_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserManagement.getInstance().requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        // 로그아웃 성공시 수행하는 지점
                        finish(); //액티비티 종료
                    }
                });
            }
        });

    }
}