package com.github.ali77gh.unitools.core.alarmAndAutoSilent;

import android.content.Context;
import android.os.PowerManager;

abstract class MyWakeLock {

    private static PowerManager.WakeLock wakeLock;

    static void acquire(Context c) {
        if (wakeLock != null) wakeLock.release();

        PowerManager pm = (PowerManager) c.getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, ":myAppTag");
        wakeLock.acquire();
    }

    static void release() {
        if (wakeLock != null) {
            wakeLock.release();
        }
        wakeLock = null;
    }
}
