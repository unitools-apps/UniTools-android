package com.github.ali77gh.unitools.core.alarm;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.core.tools.Sort;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.model.UserInfo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

import java.util.List;

/**
 * Created by ali77gh on 11/25/18.
 */

class CloseToClassNotify {

    static void on15Min(Context context){
        ContextHolder.initStatics(context);
        List<UClass> classes = UserInfoRepo.getUserInfo().Classes;
        if(classes.size()==0) return;
        Sort.SortClass(classes);
        PushNotify(context,classes.get(0));
    }

    private static void PushNotify(Context context, UClass nextClass) {

        UserInfo ui = UserInfoRepo.getUserInfo();
        if (ui.NotificationMode == UserInfo.NOTIFICATION_NOTHING) return;

        int def = nextClass.time.getMins() - DateTimeTools.getCurrentTime().getMins();
        int defConfig = ui.ReminderInMins;
        if (def > defConfig || def < 0) return;

        String title = context.getString(R.string.next_class_is_close);
        String body = Translator.getUClassReadable(nextClass);

        NotificationManager NM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notify = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.notification_icon)
                .build();

        if (ui.NotificationMode == UserInfo.NOTIFICATION_WITH_SOUND)
            notify.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        NM.notify(0, notify);
    }
}