package com.github.ali77gh.unitools.core.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.ali77gh.unitools.core.CH;

public class VoiceRecorderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (VoiceRecorder.isRecording())
            VoiceRecorder.Stop();
        CH.toast("recoding stopped");

    }
}
