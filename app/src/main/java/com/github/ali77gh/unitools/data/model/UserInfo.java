package com.github.ali77gh.unitools.data.model;

import java.util.List;

/**
 * Created by ali on 10/4/18.
 */

public class UserInfo {

    public boolean DarkTheme;
    public String LangId; // "fa" , "en" ,...
    public int NotificationMode;
    public int FirstDayOfUni; // is like unix time but in days
    public int ReminderInMins;
    public boolean AutoSilent;
    public char Calendar; // 'g' means gregorian and 'j' means jalali

    public final static int NOTIFICATION_WITH_SOUND = 0;
    public final static int NOTIFICATION_JUST_NOTIFY = 1;
    public final static int NOTIFICATION_NOTHING = 2;
}
