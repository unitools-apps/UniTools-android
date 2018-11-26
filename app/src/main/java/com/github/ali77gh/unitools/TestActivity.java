package com.github.ali77gh.unitools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.ali77gh.unitools.core.alarm.Alarm15MinRepeat;
import com.github.ali77gh.unitools.core.alarm.CloseToClassAlert;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Alarm15MinRepeat alarm = new Alarm15MinRepeat();

        findViewById(R.id.test_start).setOnClickListener(view -> {
            alarm.start(this);
        });

        findViewById(R.id.test_stop).setOnClickListener(view -> {
            alarm.stop(this);
        });
    }
}
