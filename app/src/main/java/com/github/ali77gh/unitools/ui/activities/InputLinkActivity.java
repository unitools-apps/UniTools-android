package com.github.ali77gh.unitools.ui.activities;

import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.MyDataBeen;
import com.github.ali77gh.unitools.core.StringCoder;
import com.github.ali77gh.unitools.data.model.Friend;
import com.github.ali77gh.unitools.data.repo.FriendRepo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.List;
import java.util.Locale;

public class InputLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CH.initStatics(this);
        setContentView(R.layout.activity_input_link);
        SetupLang();
        MyDataBeen.onAppStarts(this);

        EditText name = findViewById(R.id.text_input_link_activity);
        Button add = findViewById(R.id.btn_input_link_activity_add);
        Button cancel = findViewById(R.id.btn_input_link_activity_cancel);

        cancel.setOnClickListener(v -> finish());

        Friend.MinimalFriend minimalFriend;
        try {
            Uri data = getIntent().getData();

            String friendStrBase64 = data.toString().split("unitools/")[1];
            String friendStr = StringCoder.Decode(friendStrBase64);
            minimalFriend = new Gson().fromJson(friendStr, Friend.MinimalFriend.class);
        } catch (JsonParseException e) {
            CH.toast(R.string.cant_add_friend);
            finish();
            return;
        }

        Friend friend = Friend.MinimalToFull(minimalFriend);

        add.setOnClickListener(v -> {

            if (CheckFriendExist(name.getText().toString())){
                CH.toast(R.string.exists);
                return;
            }

            friend.name = name.getText().toString();
            FriendRepo.Insert(friend);
            MyDataBeen.onNewAddFriendWithLink();
            CH.toast(R.string.friend_added_successfully);
            finish();
        });

    }

    private boolean CheckFriendExist(String name){

        List<Friend> friends = FriendRepo.getAll();
        for(Friend friend : friends){
            if (friend.name.equals(name)) return true;
        }
        return false;
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
