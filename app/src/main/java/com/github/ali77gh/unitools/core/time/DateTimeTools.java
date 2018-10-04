package com.github.ali77gh.unitools.core.time;

import android.webkit.JavascriptInterface;

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
        return new Time(d.getHours(), d.getMinutes());
    }

    public static String getDayString(int day) {

        //todo to english string
        switch (day) {
            case 0:
                return "شنبه";
            case 1:
                return "یک شنبه";
            case 2:
                return "دوشنبه";
            case 3:
                return "سه شنبه";
            case 4:
                return "چهارشنبه";
            case 5:
                return "پنجشنبه";
            case 6:
                return "جمعه";
            default:
                throw new IllegalArgumentException("day should be  6>day>0 ");
        }
    }

}
