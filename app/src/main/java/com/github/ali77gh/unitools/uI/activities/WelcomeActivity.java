package com.github.ali77gh.unitools.uI.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.Model.UserInfo;
import com.github.ali77gh.unitools.data.Repo.UserInfoRepo;

/**
 * this activity runs in first time
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button ok = findViewById(R.id.btn_welcome_ok);

        ok.setOnClickListener(view -> {
            startActivity(new Intent(this, HomeActivity.class));
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        });

        // init account
        new Thread(() -> {
            UserInfo userInfo = new UserInfo();
            userInfo.LangId = getString(R.string.LangID);
            userInfo.NotificationMode = UserInfo.NOTIFICATION_NOTHING;
            userInfo.DarkTheme = true;
            new UserInfoRepo(this).setUserInfo(userInfo);
        }).start();
    }
}
