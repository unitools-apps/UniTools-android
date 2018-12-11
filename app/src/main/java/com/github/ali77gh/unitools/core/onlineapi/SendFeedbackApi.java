package com.github.ali77gh.unitools.core.onlineapi;

import android.app.Activity;
import android.provider.Settings;

/**
 * Created by ali77gh on 12/11/18.
 */

public class SendFeedbackApi {

    public static void Send(Activity activity, String message, Promise<String> callback) {

        new Thread(() -> {
            String androidId = Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID);
            //todo send http request
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            activity.runOnUiThread(() -> callback.onSuccess(""));

        }).start();
    }
}
