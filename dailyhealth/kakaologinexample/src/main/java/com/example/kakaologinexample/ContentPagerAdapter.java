package com.example.kakaologinexample;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.kakaologinexample.tab.ChatFragment;
import com.example.kakaologinexample.tab.CheckFragment;
import com.example.kakaologinexample.tab.FriendFragment;

public class ContentPagerAdapter extends FragmentStateAdapter {
    private int mPageCount = 3;

    public ContentPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                FriendFragment friendFragment = new FriendFragment();
                return friendFragment;
            case 1:
                ChatFragment chatFragment = new ChatFragment();
                return chatFragment;
            case 2:
                CheckFragment checkFragment = new CheckFragment();
                return checkFragment;
            default: return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return mPageCount;
    }
}
