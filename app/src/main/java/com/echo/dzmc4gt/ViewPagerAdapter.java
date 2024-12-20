package com.echo.dzmc4gt;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private ArrayList<Fragment> fragmentList;
    //private ArrayList<String> titleList;

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, @NonNull ArrayList<Fragment> fragments) {
        super(fragmentActivity);
        fragmentList = fragments;
        //titleList = titles;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}
