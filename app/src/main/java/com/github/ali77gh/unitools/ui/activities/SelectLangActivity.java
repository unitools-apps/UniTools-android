package com.github.ali77gh.unitools.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.data.model.UserInfo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.github.ali77gh.unitools.ui.animation.SelectLanguageCollapse;

/**
 * this activity runs in first time
 */
public class SelectLangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lang);
        CH.initStatics(this);

        Button fa = findViewById(R.id.btn_select_lang_fa);
        Button en = findViewById(R.id.btn_select_lang_en);
        View circleAnim = findViewById(R.id.view_select_lang_circle_anim);

        en.setOnClickListener(view -> {
            InitConfig("en");
            ShowGuid();
        });

        fa.setOnClickListener(view -> {
            InitConfig("fa");
            ShowGuid();
        });

        circleAnim.postDelayed(() -> SelectLanguageCollapse.collapse(circleAnim), 500);
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
        userInfo.DarkTheme = true;
        userInfo.FirstDayOfUni = 0;
        userInfo.Calendar = lang.equals("fa") ? 'j' : 'g'; // default calendar for persian is jalali and for english is miladi
        userInfo.AlwaysUpNotification = false;
        UserInfoRepo.setUserInfo(userInfo);
    }
}
