package com.ali.uneversaldatetools.datePicker;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> _mFragmentList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return _mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return _mFragmentList.size();
    }


    public void AddFragmentToEnd(Fragment fragment) {
        _mFragmentList.add(fragment);
    }
    public void AddFragmentToStart(Fragment fragment){
        _mFragmentList.add(0,fragment);
    }
}