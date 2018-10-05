package com.github.ali77gh.unitools;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.github.ali77gh.unitools.core.audio.VoicePlayer;
import com.github.ali77gh.unitools.core.audio.VoiceRecorder;


public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        VoiceRecorder voiceRecorder = new VoiceRecorder(this);
        VoicePlayer voicePlayer = new VoicePlayer(this);

        voiceRecorder.Record();

        findViewById(android.R.id.content).postDelayed(() -> {
            voiceRecorder.stop();
            voiceRecorder.SaveTempAs("uyuti");
            voicePlayer.play("uyuti");
        }, 10000);

    }
}
