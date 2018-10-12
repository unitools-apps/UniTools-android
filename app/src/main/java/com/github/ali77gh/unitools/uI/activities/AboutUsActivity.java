package com.github.ali77gh.unitools.uI.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.github.ali77gh.unitools.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        LinearLayout github = findViewById(R.id.linear_about_github);
        LinearLayout playStore = findViewById(R.id.linear_about_play_store);

        github.setOnClickListener(view -> OpenGithub());

        playStore.setOnClickListener(view -> OpenPlayStore());

    }

    private void OpenGithub() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ali77gh/UniTools"));
        startActivity(browserIntent);
    }

    private void OpenPlayStore() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://todoPlayStoreLinkHere"));
        startActivity(browserIntent);
    }
}
