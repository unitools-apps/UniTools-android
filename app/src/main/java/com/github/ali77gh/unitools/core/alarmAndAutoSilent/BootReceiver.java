package com.github.ali77gh.unitools.core.alarmAndAutoSilent;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Objects;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
            // on device boot complete, reset the alarm

            Log.d("bootReceiver", "onReceive");
            ReminderAndAutoSilent.Setup(context);
        }
    }
}
