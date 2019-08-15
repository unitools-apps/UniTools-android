package com.github.ali77gh.unitools.core;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.repo.UClassRepo;
import com.github.ali77gh.unitools.ui.activities.SplashActivity;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AppNotification {

    private final static int CUSTOM_NEWS = 2001;
    private final static int NEXT_CLASS_IS_CLOSE = 2002;
    private final static int UPDATE_IS_AVAILABLE = 2003;
    private final static int ALWAYS_UP_NOTIFICATION = 2004;

    private static final String WEBSITE = "https://unitools.ir";


    private static void ShowNotify(String title, String text, @Nullable Intent intent, int id,boolean cancelable) {
        String CHANNEL_ID = "my_channel_01";
        Context context = CH.getAppContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        android.app.Notification.Builder builder = new android.app.Notification.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.notification_icon);
        if (!cancelable) builder.setOngoing(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }

        //on notification click
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
        }

        android.app.Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id, notificationCompat);
    }

    private static void ShowNotify(String title, String text, @Nullable Intent intent, int id){
        ShowNotify(title,text,intent,id,true);
    }

    public static void ShowCustomNewsNotification(String title, String text, String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        ShowNotify(title, text, intent, CUSTOM_NEWS);
    }

    public static void ShowNextClassNotification(String uClassId) {
        ShowNotify(
                CH.getString(R.string.next_class_is_close),
                Translator.getUClassReadable(UClassRepo.getById(uClassId)),
                null,
                NEXT_CLASS_IS_CLOSE
        );
    }

    public static void ShowUpdateAvailable() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(WEBSITE));
        ShowNotify(
                CH.getString(R.string.update_available),
                CH.getString(R.string.open_on_website),
                intent,
                UPDATE_IS_AVAILABLE
        );
    }

    public static void ShowAlwaysUpNotification(String nextClassString){
        ShowNotify(
                CH.getString(R.string.next_class),
                nextClassString,
                new Intent(CH.getAppContext(), SplashActivity.class),
                ALWAYS_UP_NOTIFICATION,
                false
        );
    }

    public static void HideAlwaysUpNotification(){
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(CH.getAppContext());
        managerCompat.cancel(ALWAYS_UP_NOTIFICATION);
    }
}
