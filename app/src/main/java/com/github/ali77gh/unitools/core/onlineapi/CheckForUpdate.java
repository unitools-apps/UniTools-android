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

    private static final String API_URL = "https://unitools-apps.github.io/Website/config/version.txt";
    private static final String ON_CLICK = "https://unitools-apps.github.io/Website/";

    public static void Check() {

        new Thread(() -> {
            try {
                URL url = new URL(API_URL);
                BufferedReader in =
                        new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder page = new StringBuilder();
                String inLine;

                while ((inLine = in.readLine()) != null) {
                    page.append(inLine);
                }

                in.close();
                if (!ContextHolder.getAppContext().getString(R.string.app_version).equals(page.toString()))
                    PushNotify();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void PushNotify() {

        NotificationManager NM = (NotificationManager) ContextHolder.getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder
                (ContextHolder.getAppContext())
                .setContentTitle(ContextHolder.getAppContext().getString(R.string.update_available))
                .setContentText(ContextHolder.getAppContext().getString(R.string.open_on_website))
                .setSmallIcon(R.drawable.notification_icon).build();

        //put link on notification click
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ON_CLICK));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notify.contentIntent = PendingIntent.getActivity(ContextHolder.getAppContext(),1, intent, 0);

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        NM.notify(0, notify);

    }
}
