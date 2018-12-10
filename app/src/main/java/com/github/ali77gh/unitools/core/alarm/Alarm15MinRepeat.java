package com.github.ali77gh.unitools.core.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by ali77gh on 11/25/18.
 */

public class Alarm15MinRepeat extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

//        //this part runs every 15 min
        CloseToClassAlert.on15Min(context);
        Silent.on15Min(context);
//
    }

    public void start(Context context) {

        stop(context); // if there is a alarm active
        int min = new Date().getMinutes();
        if (min < 15) {
            min = 16 - min;
        } else if (min >= 15 && min < 30) {
            min = 31 - min;
        } else if (min >= 30 && min < 45) {
            min = 46 - min;
        } else { //45 -> 60
            min = 61 - min;
        }

        setAlarm(context, min);
    }

    public void stop(Context context) {
        Intent intent = new Intent(context, Alarm15MinRepeat.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setAlarm(Context context, int min) {
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 15;

        /* Set the alarm to start at 10:30 AM */

        Intent alarmIntent = new Intent(context, Alarm15MinRepeat.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis() + min * 60 * 1000,
                interval, pendingIntent);
    }

    public void TestNotifi(Context context) {

        Toast.makeText(context, "hi", Toast.LENGTH_SHORT).show();

        NotificationManager NM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder
                (context).setContentTitle("test").setSmallIcon(R.mipmap.ic_launcher).
                setContentText(now()).build();


        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        NM.notify(new Random().nextInt(100), notify);
    }

    private String now() {
        return new Date().toString();
    }
}
