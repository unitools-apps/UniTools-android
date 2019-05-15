package com.github.ali77gh.unitools.ui.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        CH.initStatics(this);
        Next();
        Animate();
    }

    private void Animate(){
        ImageView pencil = findViewById(R.id.splash_logo_item_1);
        ImageView pen = findViewById(R.id.splash_logo_item_2);
        ImageView ruler = findViewById(R.id.splash_logo_item_3);

        pencil.animate().rotation(0).setDuration(200).setStartDelay(300).setInterpolator(new OvershootInterpolator()).start();
        pen.animate().rotation(0).setDuration(200).setStartDelay(500).setInterpolator(new OvershootInterpolator()).start();
        ruler.animate().rotation(0).setDuration(200).setStartDelay(700).setInterpolator(new OvershootInterpolator()).start();
    }

    private void Next() {
        findViewById(android.R.id.content).postDelayed(() -> {
            if (UserInfoRepo.getUserInfo() == null) {
                startActivity(new Intent(this, SelectLangActivity.class));
                overridePendingTransition(R.anim.no_animation, R.anim.no_animation);
            } else {
                SetupLang();
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        }, 1000);
    }

    private void SetupLang() {

        String lang = UserInfoRepo.getUserInfo().LangId;
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang)); // API 17+ only
        res.updateConfiguration(conf, dm);
    }
}
