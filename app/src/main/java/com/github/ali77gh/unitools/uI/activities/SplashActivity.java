package com.github.ali77gh.unitools.uI.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.data.Repo.EventRepo;
import com.github.ali77gh.unitools.data.Repo.FileRepo;
import com.github.ali77gh.unitools.data.Repo.FriendRepo;
import com.github.ali77gh.unitools.data.Repo.UserInfoRepo;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ContextHolder.initStatics(this);
        Next();
    }

    private void Next() {
        findViewById(android.R.id.content).postDelayed(() -> {
            if (UserInfoRepo.getUserInfo() == null) {
                startActivity(new Intent(this, WelcomeActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                SetupLang();
                startActivity(new Intent(this, HomeActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        }, 500);
    }

    private void SetupLang() {

        String lang = UserInfoRepo.getUserInfo().LangId;

        Resources res = getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }
}
