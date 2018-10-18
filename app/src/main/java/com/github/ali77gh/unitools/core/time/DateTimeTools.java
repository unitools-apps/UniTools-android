package com.github.ali77gh.unitools.core.time;

import com.github.ali77gh.unitools.data.Model.Time;

import java.util.Date;

/**
 * Created by ali on 10/4/18.
 */

public class DateTimeTools {

    public static int getCurrentDayOfWeek() {
        return new Date().getDay();
    }

    public static Time getCurrentTime() {
        Date d = new Date();
        return new Time(getCurrentDayOfWeek(),d.getHours(), d.getMinutes());
    }
}
