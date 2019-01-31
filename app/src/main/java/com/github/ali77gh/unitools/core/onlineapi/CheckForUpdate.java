package com.github.ali77gh.unitools.core.onlineapi;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class CheckForUpdate {


    public static void Check() {

        new Thread(() -> {
            try {
                URL url = new URL("https://unitools-apps.github.io/Website/config/version.txt");
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder page = new StringBuilder();
                String inLine;

                while ((inLine = in.readLine()) != null) {
                    page.append(inLine);
                }

                in.close();
                if (!ContextHolder.getAppContext().getString(R.string.app_version).equals(page.toString()))
                    PushNotifi();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void PushNotifi() {

        NotificationManager NM = (NotificationManager) ContextHolder.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder
                (ContextHolder.getAppContext())
                .setContentTitle("UniTools update available")
                .setContentText("click to open website")
                .setSmallIcon(R.drawable.logo_icon).build();

        //put link on notification click
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://unitools-apps.github.io/Website/"));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notify.contentIntent = PendingIntent.getActivity(ContextHolder.getAppContext(),1, intent, 0);

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        NM.notify(0, notify);

    }
}
