package com.github.ali77gh.unitools.core.alarmAndAutoSilent;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;

import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.AppNotification;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.audio.SilentManager;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.repo.UClassRepo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Intent.FLAG_INCLUDE_STOPPED_PACKAGES;
import static com.github.ali77gh.unitools.data.model.UClass.ReminderValues.DISABLE_REMINDER;

/**
 * Created by ali77gh on 11/25/18.
 */

public class ReminderAndAutoSilent extends BroadcastReceiver {

    private static final String MODE = "mode";
    private static final String CLASS_ID = "classId";
    private static final int MODE_SILENT = 1;
    private static final int MODE_ALERT = 2;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receive_time", new Date().toString());
        MyWakeLock.acquire(context);

        CH.initStatics(context);
        SetupLang(context);
        switch (intent.getIntExtra(MODE, -1)) {
            case MODE_ALERT:
                String classId = intent.getStringExtra(CLASS_ID);
                if (classId != null)
                    AppNotification.ShowNextClassNotification(classId);
                else
                    throw new RuntimeException("classId not found");
                break;
            case MODE_SILENT:
                SilentManager.Vibrate(context);
                break;
            default:
                throw new RuntimeException("invalid MODE:-1");
        }


        MyWakeLock.release();
    }

    public static void Setup(Context context) {

        CH.initStatics(context);
        //first clear old alarms
        Clear(context);

        //set new alarms
        int index = 0;
        for (UClass uClass : UClassRepo.getAll()) {

            if (UserInfoRepo.getUserInfo().AutoSilent) {
                SetAlarm(context, MODE_SILENT, uClass.id, index, NextClassUnixTime(uClass));
                index++;
            }
            if (uClass.reminder != DISABLE_REMINDER) {
                SetAlarm(context, MODE_ALERT, uClass.id, index, NextClassUnixTime(uClass) - uClass.reminder);
                index++;
            }
        }

        //To enable Boot Receiver class
        PackageManager pm = context.getPackageManager();
        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
        //endregion
    }

    private static long NextClassUnixTime(UClass uClass) {
        int diffMin;
        final int WEEK_MIN = 7 * 24 * 60;
        if (DateTimeTools.getCurrentTime().getMins() < uClass.time.getMins())
            diffMin = uClass.time.getMins() - DateTimeTools.getCurrentTime().getMins();
        else
            diffMin = (uClass.time.getMins() - DateTimeTools.getCurrentTime().getMins()) + WEEK_MIN;

        long unixTime = new Date().getTime() + (diffMin * 60 * 1000);
        Log.d("NextClassUnixTime", "--");
        Log.d("NextClassUnixTime", String.valueOf(unixTime));
        Log.d("NextClassUnixTime", new JalaliDateTime((int) unixTime * 1000, TimeZone.getDefault()).toString());
        return new Date().getTime() + (diffMin * 60 * 1000);
    }

    public static void Clear(Context context) {

        for (int i = 0; i < 30; i++) {
            Intent intent = new Intent(context, ReminderAndAutoSilent.class);
            PendingIntent sender = PendingIntent.getBroadcast(context, i, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.cancel(sender);
        }
    }

    private static void SetAlarm(Context context, int mode, String classId, int id, long unixTime) {

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, ReminderAndAutoSilent.class);
        alarmIntent.setFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
        alarmIntent.putExtra(MODE, mode);
        alarmIntent.putExtra(CLASS_ID, classId);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, alarmIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_UPDATE_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            manager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, unixTime, pendingIntent);
        else
            manager.set(AlarmManager.RTC_WAKEUP, unixTime, pendingIntent);

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
