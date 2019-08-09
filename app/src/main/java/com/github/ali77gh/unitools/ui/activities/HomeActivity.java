package com.github.ali77gh.unitools.ui.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.RemoteViews;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.MyDataBeen;
import com.github.ali77gh.unitools.core.alarmAndAutoSilent.ReminderAndAutoSilent;
import com.github.ali77gh.unitools.core.onlineapi.CheckForUpdate;
import com.github.ali77gh.unitools.core.onlineapi.GetCustomNews;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.github.ali77gh.unitools.ui.NavToViewPagerBinder;
import com.github.ali77gh.unitools.ui.adapter.ViewPagerAdapter;
import com.github.ali77gh.unitools.ui.fragments.Backable;
import com.github.ali77gh.unitools.ui.fragments.DocsFragment;
import com.github.ali77gh.unitools.ui.fragments.SettingsFragment;
import com.github.ali77gh.unitools.ui.fragments.WallFragment;
import com.github.ali77gh.unitools.ui.widget.ShowNextClassWidget;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private BottomNavigationView navigation;
    private ViewPagerAdapter viewPagerAdapter;
    public static Backable currentFrag;
    private final int DEFAULT_NAV_MENU = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CH.initStatics(this);
        SetupNav();
        CheckForUpdate.Check();
        GetCustomNews.Check();
        ReminderAndAutoSilent.Setup(this);

        MyDataBeen.onAppStarts(this);
    }

    private void SetupNav() {

        viewpager = findViewById(R.id.viewpager_home);
        navigation = findViewById(R.id.navigation_home);

        WallFragment wallFragment = new WallFragment();
        DocsFragment docsFragment = new DocsFragment();
        SettingsFragment settingsFragment = new SettingsFragment();

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.AddFragment(wallFragment);
        viewPagerAdapter.AddFragment(docsFragment);
        viewPagerAdapter.AddFragment(settingsFragment);

        NavToViewPagerBinder.Bind(navigation, viewpager,viewPagerAdapter);

        viewpager.post(() -> viewpager.setCurrentItem(DEFAULT_NAV_MENU, false));
        navigation.setSelectedItemId(R.id.navigation_home);

        currentFrag = wallFragment;
        viewpager.setAdapter(viewPagerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() != null)
                if (currentFrag instanceof WallFragment)
                    ((WallFragment) currentFrag).OnBarcodeReaded(result.getContents());
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (currentFrag instanceof DocsFragment) {
            ((DocsFragment) currentFrag).CheckPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SetupLang();
    }

    private void SetupLang() {

        String lang = UserInfoRepo.getUserInfo().LangId;

        if (lang.equals(getString(R.string.LangID))) return;

        Resources res = getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
        recreate();
    }

    @Override
    public void onBackPressed() {
        if (!currentFrag.onBack()) {

            if (viewpager.getCurrentItem() != DEFAULT_NAV_MENU) {
                viewpager.setCurrentItem(DEFAULT_NAV_MENU, true);
                return;
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        updateWidgets();// because classes may changed
        ReminderAndAutoSilent.Setup(this); // because classes may changed
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyDataBeen.onAppStops();
    }

    protected void updateWidgets() {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.widget_show_next_class);
        ComponentName thisWidget = new ComponentName(this, ShowNextClassWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = manager.getAppWidgetIds(thisWidget);
        manager.partiallyUpdateAppWidget(appWidgetIds, remoteViews);

        ShowNextClassWidget.Update(this, manager, appWidgetIds);
    }

}
