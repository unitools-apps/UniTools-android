package com.github.ali77gh.unitools.data.Model;

import java.util.List;

/**
 * Created by ali on 10/4/18.
 */

public class UserInfo {

    public boolean DarkTheme;
    public String LangId; // "fa" , "en" ,...
    public List<UClass> Classes;
    public int NotificationMode;
    public int FirstDayOfUni;

    public final static int NOTIFICATION_WITH_SOUND = 0;
    public final static int NOTIFICATION_JUST_NOTIFI = 1;
    public final static int NOTIFICATION_NOTHING = 2;
}
