package com.github.ali77gh.unitools.ui;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.ui.activities.HomeActivity;
import com.github.ali77gh.unitools.ui.adapter.ViewPagerAdapter;
import com.github.ali77gh.unitools.ui.fragments.Backable;

/**
 * Created by ali77gh on 1/21/19.
 */

public class NavToViewPagerBinder {

    public static void Bind(BottomNavigationView navigation, ViewPager viewpager, ViewPagerAdapter adapter){

        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewpager.setCurrentItem(0, true);
                    HomeActivity.currentFrag = (Backable) adapter.getItem(0);
                    break;
                case R.id.navigation_storage:
                    viewpager.setCurrentItem(1, true);
                    HomeActivity.currentFrag = (Backable) adapter.getItem(1);
                    break;
                case R.id.navigation_settings:
                    viewpager.setCurrentItem(2, true);
                    HomeActivity.currentFrag = (Backable) adapter.getItem(2);
                    break;

                default:
                    throw new RuntimeException("invalid menu");
            }
            return true;
        };

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        navigation.setSelectedItemId(R.id.navigation_home);
                        break;
                    case 1:
                        navigation.setSelectedItemId(R.id.navigation_storage);
                        break;
                    case 2:
                        navigation.setSelectedItemId(R.id.navigation_settings);
                        break;
                    default:
                        throw new IllegalArgumentException("invalid page");
                }
                HomeActivity.currentFrag = (Backable) adapter.getItem(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        };

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewpager.addOnPageChangeListener(onPageChangeListener);
    }
}
