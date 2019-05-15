package com.github.ali77gh.unitools.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali77gh on 11/9/18.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter  {

    private List<Fragment> _mFragmentList = new ArrayList<>();
    private List<String> _mFragmentTitles = new ArrayList<>();

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

    @Override
    public CharSequence getPageTitle(int position) {
        return _mFragmentTitles.get(position);
    }

    public void AddFragment(Fragment fragment) {
        _mFragmentList.add(fragment);
        _mFragmentTitles.add("");
    }
    public void AddFragment(Fragment fragment,String title) {
        _mFragmentList.add(fragment);
        _mFragmentTitles.add(title);
    }

}
