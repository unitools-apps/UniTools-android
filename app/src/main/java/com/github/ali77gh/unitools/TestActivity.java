package com.github.ali77gh.unitools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.ali77gh.unitools.core.audio.VoiceRecorder;

import java.io.IOException;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        VoiceRecorder voiceRecorder = new VoiceRecorder(this);

        findViewById(android.R.id.content).postDelayed(() -> {
            try {
                voiceRecorder.Record();
            } catch (IOException e) {
                Toast.makeText(this, "record failed", Toast.LENGTH_SHORT).show();
            }
        },2000);

        findViewById(android.R.id.content).postDelayed(() -> {
            voiceRecorder.StopRecord();
        },4000);

        findViewById(android.R.id.content).postDelayed(() -> {
            voiceRecorder.Play();
        },5000);
    }
}
