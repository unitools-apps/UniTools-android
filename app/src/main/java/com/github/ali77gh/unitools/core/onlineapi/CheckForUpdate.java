package com.github.ali77gh.unitools.core.onlineapi;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

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
                if (!CH.getString(R.string.app_version).equals(page.toString()))
                    PushNotify();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static void PushNotify() {

        String CHANNEL_ID = "my_channel_01";
        Context context = CH.getAppContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(CH.getString(R.string.update_available));
        builder.setContentText(CH.getString(R.string.open_on_website)); // todo replace with context.getString(R.string.next_class_is_close)
        builder.setSmallIcon(R.drawable.notification_icon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }

        //put link on notification click
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ON_CLICK));
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //to be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        builder.setContentIntent(pendingIntent);

        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(new Random().nextInt(), notificationCompat);
    }
}
