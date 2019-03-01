package com.github.ali77gh.unitools.core;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;
import android.util.DisplayMetrics;
import android.util.Log;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.audio.SilentManager;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.model.UserInfo;
import com.github.ali77gh.unitools.data.repo.UClassRepo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.app.AlarmManager.INTERVAL_DAY;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by ali77gh on 11/25/18.
 */

public class ReminderAndAutoSilent extends BroadcastReceiver {

    private static final String MODE = "mode";
    private static final String CLASS_ID = "classId";
    private static final int MODE_SILENT = 1;
    private static final int MODE_ALERT = 2;

    private static final long INTERVAL_WEEK = INTERVAL_DAY * 7;

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i("ReminderAndAutoSilent","onReceive()");
        if (ContextHolder.getAppContext() == null) ContextHolder.initStatics(context);
        SetupLang(context);

        UserInfo ui = UserInfoRepo.getUserInfo();
        switch (intent.getIntExtra(MODE,3)){

            case MODE_ALERT:
                String id = intent.getStringExtra(CLASS_ID);
                if (id == null) throw new IllegalStateException("class id should provide");
                if (ui.NotificationMode != UserInfo.NOTIFICATION_NOTHING)
                    PushNotify(context, Translator.getUClassReadable(UClassRepo.getById(id)));
                break;

            case MODE_SILENT:
                if (ui.AutoSilent)
                    SilentManager.Silent(context);
                break;

            default:
                throw new IllegalArgumentException("invalid mode");

        }

    }

    public static void Reset(Context context) {
        //first clear old alarms
        Clear(context);

        if (ContextHolder.getAppContext() == null) ContextHolder.initStatics(context);
        UserInfo userInfo = UserInfoRepo.getUserInfo();

        if (UClassRepo.getAll().size()==0) return;
        //setup alarms
        for (UClass uclass : UClassRepo.getAll()){

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            //silent

            Intent notifyIntent = new Intent(context , ReminderAndAutoSilent.class);
            notifyIntent.putExtra(MODE , MODE_SILENT);
            PendingIntent pendingIntent = PendingIntent.getBroadcast
                    (context,  UClassRepo.getAll().indexOf(uclass) * 2 , notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        NextClassUnixTime(uclass),
                        pendingIntent
                );
            } else {
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        NextClassUnixTime(uclass),
                        INTERVAL_WEEK,
                        pendingIntent
                );
            }


            //reminder
            Intent notifyIntent2 = new Intent(context , ReminderAndAutoSilent.class);
            notifyIntent2.putExtra(MODE , MODE_ALERT);
            notifyIntent2.putExtra(CLASS_ID , uclass.id);
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast
                    (context, (UClassRepo.getAll().indexOf(uclass) * 2 ) - 1 , notifyIntent2, PendingIntent.FLAG_UPDATE_CURRENT);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        NextClassUnixTime(uclass) - userInfo.ReminderInMins * 60 * 1000,
                        pendingIntent
                );
            } else {
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        NextClassUnixTime(uclass) - userInfo.ReminderInMins * 60 * 1000,
                        INTERVAL_WEEK,
                        pendingIntent2
                );
            }
        }

    }

    private static long NextClassUnixTime(UClass uClass){
        int diffMin;
        final int WEEK_MIN = 7 * 24 * 60 ;
        if (DateTimeTools.getCurrentTime().getMins() < uClass.time.getMins())
            diffMin = uClass.time.getMins() - DateTimeTools.getCurrentTime().getMins();
        else
            diffMin = (uClass.time.getMins() - DateTimeTools.getCurrentTime().getMins()) + WEEK_MIN;

        return new Date().getTime() + (diffMin * 60 * 1000);
    }

    public static void Clear(Context context) {

        for(int i = 0 ; i < 30 ; i++){
            Intent intent = new Intent(context, ReminderAndAutoSilent.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, i, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(sender);
        }
    }

    private static void PushNotify(Context context , String nextClassInfo) {
        String CHANNEL_ID = "my_channel_01";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(nextClassInfo);
        builder.setContentText(context.getString(R.string.next_class_is_close));
        builder.setSmallIcon(R.drawable.notification_icon);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }
        Notification notificationCompat = builder.build();
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(new Random().nextInt(), notificationCompat);
    }

    private static void SetupLang(Context context) {

        String lang = UserInfoRepo.getUserInfo().LangId;

        if (lang.equals(context.getString(R.string.LangID))) return;

        Resources res = context.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang)); // API 17+ only.
        // Use conf.locale = new Locale(...) if targeting lower versions
        res.updateConfiguration(conf, dm);
    }
}
