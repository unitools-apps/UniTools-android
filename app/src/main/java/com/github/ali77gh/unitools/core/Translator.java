package com.github.ali77gh.unitools.core;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.data.Model.Event;
import com.github.ali77gh.unitools.data.Model.UClass;

/**
 * Created by ali77gh on 10/12/18.
 */

public class Translator {

    public static String getUClassReadable(UClass uClass) {
        String day;
        if (DateTimeTools.getCurrentDayOfWeek() == uClass.time.dayOfWeek) {
            day = ContextHolder.getAppContext().getString(R.string.today);
        } else if (DateTimeTools.getCurrentDayOfWeek() + 1 == uClass.time.dayOfWeek | (DateTimeTools.getCurrentDayOfWeek() == 6 & 0 == uClass.time.dayOfWeek)) {
            day = ContextHolder.getAppContext().getString(R.string.tomorrow);
        } else {
            day = getDayString(uClass.time.dayOfWeek);
        }

        return day + " " + NumToString(uClass.time.hour) + ":" + NumToString(uClass.time.min) + " " + uClass.what + " " + uClass.where;
    }

    public static String getEventReadable(Event event) {
        String day;
        if (DateTimeTools.getCurrentDayOfWeek() == event.time.dayOfWeek) {
            day = ContextHolder.getAppContext().getString(R.string.today);
        } else if (DateTimeTools.getCurrentDayOfWeek() + 1 == event.time.dayOfWeek | (DateTimeTools.getCurrentDayOfWeek() == 6 & 0 == event.time.dayOfWeek)) {
            day = ContextHolder.getAppContext().getString(R.string.tomorrow);
        } else {
            day = getDayString(event.time.dayOfWeek);
        }

        return day + " " + NumToString(event.time.hour) + ":" + NumToString(event.time.min) + " " + event.what;
    }


    public static String getDayString(int day) {
        return ContextHolder.getAppContext().getResources().getStringArray(R.array.weekDays)[day];
    }

    public static String NumToString(String s) {
        s.replace("0", ContextHolder.getAppContext().getResources().getStringArray(R.array.nums)[0]);
        s.replace("1", ContextHolder.getAppContext().getResources().getStringArray(R.array.nums)[1]);
        s.replace("2", ContextHolder.getAppContext().getResources().getStringArray(R.array.nums)[2]);
        s.replace("3", ContextHolder.getAppContext().getResources().getStringArray(R.array.nums)[3]);
        s.replace("4", ContextHolder.getAppContext().getResources().getStringArray(R.array.nums)[4]);
        s.replace("5", ContextHolder.getAppContext().getResources().getStringArray(R.array.nums)[5]);
        s.replace("6", ContextHolder.getAppContext().getResources().getStringArray(R.array.nums)[6]);
        s.replace("7", ContextHolder.getAppContext().getResources().getStringArray(R.array.nums)[7]);
        s.replace("8", ContextHolder.getAppContext().getResources().getStringArray(R.array.nums)[8]);
        s.replace("9", ContextHolder.getAppContext().getResources().getStringArray(R.array.nums)[9]);
        return s;
    }

    public static String NumToString(Number s) {
        return NumToString(s.toString());
    }


    public static String getWeekNumberString(int weekNumber) {
        if (weekNumber>100){
            return ContextHolder.getAppContext().getString(R.string.not_set);
        }
        return ContextHolder.getAppContext().getResources().getString(R.string.week) + " " + NumToString(weekNumber);
    }
}
