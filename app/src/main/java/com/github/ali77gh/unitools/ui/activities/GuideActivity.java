package com.github.ali77gh.unitools.ui.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.github.ali77gh.unitools.ui.adapter.ImageAdapter;
import com.rbrooks.indefinitepagerindicator.IndefinitePagerIndicator;

import java.util.Locale;

public class GuideActivity extends AppCompatActivity {


    private final int[] images = new int[]{
            R.drawable.guide1,
            R.drawable.guide2,
            R.drawable.guide3,
            R.drawable.guide4,
            R.drawable.guide5,
            R.drawable.guide6,
            R.drawable.guide7,
            R.drawable.guide8
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        SetupLang();

        TextView title = findViewById(R.id.text_guid_title);
        ViewPager slider = findViewById(R.id.view_pager_guid_image_slider);
        IndefinitePagerIndicator dots = findViewById(R.id.dots_guid);
        TextView info = findViewById(R.id.text_guid_info);
        Button skip = findViewById(R.id.btn_guid_skip);


        slider.setAdapter(new ImageAdapter(this, images));

        dots.attachToViewPager(slider);
        info.post(() -> info.setText(getResources().getStringArray(R.array.guide)[0]));

        title.postDelayed(() -> title.animate().alpha(1).setDuration(300).start(),1500);

        slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                info.postDelayed(() -> info.setText(getResources().getStringArray(R.array.guide)[i]),300);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        skip.setOnClickListener(view -> {
            if (getIntent().getBooleanExtra("isFromSelectLang", false)) {
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            finish();
        });
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
}


