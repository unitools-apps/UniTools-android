package com.github.ali77gh.unitools.core;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.audio.VoiceRecorderReceiver;
import com.github.ali77gh.unitools.data.repo.UClassRepo;
import com.github.ali77gh.unitools.ui.activities.SplashActivity;

import static android.app.Notification.GROUP_ALERT_SUMMARY;
import static android.content.Context.NOTIFICATION_SERVICE;

public class AppNotification {

    private final static String CHANNEL_ID = "my_channel_01";
    private final static String CHANNEL_NAME = "unitools notification channel";

    private final static int CUSTOM_NEWS = 2001;
    private final static int NEXT_CLASS_IS_CLOSE = 2002;
    private final static int UPDATE_IS_AVAILABLE = 2003;
    private final static int ALWAYS_UP_NOTIFICATION = 2004;
    private final static int RECORDING_NOTIFICATION = 2005;

    private static final String WEBSITE = "https://unitools.ir";


    private static void ShowNotify(String title, String text, @Nullable Intent intent, int id, boolean cancelable, boolean silent, Notification.Action action) {

        Context context = CH.getAppContext();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.notification_icon);
        builder.setLargeIcon(getLargeIcon());
        builder.setShowWhen(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH && action != null)
            builder.addAction(action);
        if (silent) silentNotify(builder);
        if (!cancelable) builder.setOngoing(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
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

        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(id, notificationCompat);
    }

    public static void ShowCustomNewsNotification(String title, String text, String link) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
        ShowNotify(
                title,
                text,
                intent,
                CUSTOM_NEWS,
                true,
                true,
                null
        );
    }

    public static void ShowNextClassNotification(String uClassId) {
        ShowNotify(
                CH.getString(R.string.next_class_is_close),
                Translator.getUClassReadable(UClassRepo.getById(uClassId)),
                null,
                NEXT_CLASS_IS_CLOSE,
                true,
                false,
                null
        );
    }

    public static void ShowUpdateAvailable() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(WEBSITE));
        ShowNotify(
                CH.getString(R.string.update_available),
                CH.getString(R.string.open_on_website),
                intent,
                UPDATE_IS_AVAILABLE,
                true,
                false,
                null
        );
    }

    public static void ShowAlwaysUpNotification(String nextClassString){
        ShowNotify(
                CH.getString(R.string.next_class),
                nextClassString,
                new Intent(CH.getAppContext(), SplashActivity.class),
                ALWAYS_UP_NOTIFICATION,
                false,
                true,
                null
        );
    }

    public static void HideAlwaysUpNotification(){
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(CH.getAppContext());
        managerCompat.cancel(ALWAYS_UP_NOTIFICATION);
    }

    public static void ShowRecording() {
        Intent VoiceController = new Intent(CH.getAppContext(), VoiceRecorderReceiver.class);
        Notification.Action action = new Notification.Action(
                R.drawable.storage_voices_pause,
                "stop",
                PendingIntent.getBroadcast(
                        CH.getAppContext(),
                        87523,
                        VoiceController,
                        PendingIntent.FLAG_UPDATE_CURRENT
                )

        );

        ShowNotify(
                CH.getString(R.string.app_name),
                CH.getString(R.string.recording),
                null,
                RECORDING_NOTIFICATION,
                false,
                true,
                action
        );
    }

    public static void HideRecording() {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(CH.getAppContext());
        managerCompat.cancel(RECORDING_NOTIFICATION);
    }

    private static Bitmap getLargeIcon() {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) CH.getResources().getDrawable(R.drawable.app_logo);
        return bitmapDrawable.getBitmap();
    }

    private static void silentNotify(Notification.Builder builder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setGroupAlertBehavior(GROUP_ALERT_SUMMARY);
            builder.setGroup("My group");
            builder.setGroupSummary(false);
            builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSound(null, null);
        }
    }
}
