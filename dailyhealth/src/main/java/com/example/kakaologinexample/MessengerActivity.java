package com.example.kakaologinexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;

public class MessengerActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ContentPagerAdapter contentPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        tabLayout = findViewById(R.id.layout_tab);
        viewPager2 = findViewById(R.id.pager_content);

        contentPagerAdapter
                = new ContentPagerAdapter(this);
        viewPager2.setAdapter(contentPagerAdapter);

//        List<String> tabElement = Arrays.asList("          친구목록",
//                "          채팅","          요청");
        List<String> tabElement = Arrays.asList("친구목록", "채팅","요청");

        //tabLayout 과 viewPager 연결
        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        TextView textView = new TextView(MessengerActivity.this);
                        textView.setText(tabElement.get(position));
                        tab.setCustomView(textView);
                    }
                }).attach();
    }
}