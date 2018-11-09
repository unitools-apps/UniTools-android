package com.github.ali77gh.unitools.uI.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.data.Model.UserInfo;
import com.github.ali77gh.unitools.data.Repo.EventRepo;
import com.github.ali77gh.unitools.data.Repo.FileRepo;
import com.github.ali77gh.unitools.data.Repo.FriendRepo;
import com.github.ali77gh.unitools.data.Repo.UserInfoRepo;

import java.util.ArrayList;

/**
 * this activity runs in first time
 */
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initStatics();

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
            userInfo.Classes = new ArrayList<>();
            userInfo.FirstDayOfUni = 0;
            UserInfoRepo.setUserInfo(userInfo);
        }).start();
    }

    private void initStatics() {
        EventRepo.init(this);
        FileRepo.init(this);
        FriendRepo.init(this);
        UserInfoRepo.init(this);
        ContextHolder.init(this);
    }
}
