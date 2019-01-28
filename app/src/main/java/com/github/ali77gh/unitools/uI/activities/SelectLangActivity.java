package com.github.ali77gh.unitools.uI.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.data.model.UserInfo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.github.ali77gh.unitools.uI.animation.SelectLanguageCollapse;

import java.util.ArrayList;

/**
 * this activity runs in first time
 */
public class SelectLangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lang);
        ContextHolder.initStatics(this);

        ImageView logo = findViewById(R.id.image_select_lang_logo);
        Button fa = findViewById(R.id.btn_select_lang_fa);
        Button en = findViewById(R.id.btn_select_lang_en);
        View cicleAnim = findViewById(R.id.view_select_lang_circle_anim);

        en.setOnClickListener(view -> {
            InitConfig("en");
            logo.animate().rotation(360).setDuration(250).start();
            logo.postDelayed(this::ShowGuid, 260);
        });

        fa.setOnClickListener(view -> {
            InitConfig("fa");
            logo.animate().rotation(360).setDuration(250).start();
            logo.postDelayed(this::ShowGuid, 260);
        });

        cicleAnim.postDelayed(() -> SelectLanguageCollapse.collapse(cicleAnim), 500);
    }

    private void ShowGuid() {
        Intent intent = new Intent(this, GuideActivity.class);
        intent.putExtra("isFromSelectLang",true);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    // default settings
    private void InitConfig(String lang) {
        UserInfo userInfo = new UserInfo();
        userInfo.LangId = lang;
        userInfo.NotificationMode = UserInfo.NOTIFICATION_NOTHING;
        userInfo.DarkTheme = true;
        userInfo.Classes = new ArrayList<>();
        userInfo.FirstDayOfUni = 0;
        userInfo.ReminderInMins = 50;
        UserInfoRepo.setUserInfo(userInfo);
    }
}
