package com.github.ali77gh.unitools.uI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.data.Repo.EventRepo;
import com.github.ali77gh.unitools.data.Repo.FileRepo;
import com.github.ali77gh.unitools.data.Repo.FriendRepo;
import com.github.ali77gh.unitools.data.Repo.UserInfoRepo;
import com.github.ali77gh.unitools.uI.adapter.ViewPagerAdapter;
import com.github.ali77gh.unitools.uI.fragments.BayganiFragment;
import com.github.ali77gh.unitools.uI.fragments.JozveFragment;
import com.github.ali77gh.unitools.uI.fragments.SettingsFragment;
import com.github.ali77gh.unitools.uI.fragments.WallFragment;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class HomeActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private BottomNavigationView navigation;
    private WallFragment wallFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initStatics();
        SetupNav();
    }

    private void SetupNav() {

        viewpager = findViewById(R.id.viewpager_home);
        navigation = findViewById(R.id.navigation_home);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewpager.addOnPageChangeListener(onPageChangeListener);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        wallFragment = new WallFragment();
        BayganiFragment bayganiFragment = new BayganiFragment();
        JozveFragment jozveFragment = new JozveFragment();
        SettingsFragment settingsFragment = new SettingsFragment();
        viewPagerAdapter.AddFragment(wallFragment);
        viewPagerAdapter.AddFragment(bayganiFragment);
        viewPagerAdapter.AddFragment(jozveFragment);
        viewPagerAdapter.AddFragment(settingsFragment);
        viewpager.setAdapter(viewPagerAdapter);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                viewpager.setCurrentItem(0, true);
                return true;
            case R.id.navigation_storage:
                viewpager.setCurrentItem(1, true);
                return true;
            case R.id.navigation_docs:
                viewpager.setCurrentItem(2, true);
                return true;
            case R.id.navigation_settings:
                viewpager.setCurrentItem(3, true);
                return true;
        }
        return false;
    };

    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
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
                    navigation.setSelectedItemId(R.id.navigation_docs);
                    break;
                case 3:
                    navigation.setSelectedItemId(R.id.navigation_settings);
                    break;
                default:
                    throw new IllegalArgumentException("invalid page");
            }

        }

        @Override
        public void onPageScrollStateChanged(int i) {
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                wallFragment.OnBarcodeReaded(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initStatics() {
        EventRepo.init(this);
        FileRepo.init(this);
        FriendRepo.init(this);
        UserInfoRepo.init(this);
        ContextHolder.init(this);
    }
}
