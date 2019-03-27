package com.github.ali77gh.unitools.core;

import android.content.Context;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.data.model.Event;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

/**
 * Created by ali77gh on 10/12/18.
 */

public class Translator {

    public static String getUClassReadable(UClass uClass){
        return getUClassReadable(uClass,false);
    }

    public static String getUClassReadable(UClass uClass, boolean widget) {
        String day = "";
        String toClass = "";
        if (DateTimeTools.getCurrentDayOfWeek() == uClass.time.dayOfWeek) {

            int diff = DateTimeTools.getCurrentTime().getMins() - uClass.time.getMins();
            if (diff > 0 & diff < 60) {
                toClass = "(" + CH.getString(R.string.started) + ")";
            } else if (diff < 0) {
                toClass = "(" + String.valueOf(uClass.time.getMins() - DateTimeTools.getCurrentTime().getMins()) + " " + CH.getString(R.string.minute_later) + " )";
            } else day = CH.getString(R.string.today);
        } else if (DateTimeTools.getCurrentDayOfWeek() + 1 == uClass.time.dayOfWeek | (DateTimeTools.getCurrentDayOfWeek() == 6 & 0 == uClass.time.dayOfWeek)) {
            day = CH.getString(R.string.tomorrow);
        } else {
            day = getDayString(uClass.time.dayOfWeek);
        }

        if (widget)
            return day + " " + uClass.time.toString() + " " + uClass.what + " " + uClass.where;
        else
            return day + " " + uClass.time.toString() + " " + uClass.what + " " + uClass.where + " " + toClass;
    }

    public static String getEventReadable(Event event) {
        Context context = CH.getAppContext();
        int currentDayOfWeek = DateTimeTools.getCurrentDayOfWeek();
        int currentWeek = UserInfoRepo.getWeekNumber();

        String week;
        if (currentWeek == event.WeekNumber) {
            week = context.getString(R.string.this_week);
        } else if (currentWeek + 1 == event.WeekNumber) {
            week = context.getString(R.string.next_week);
        } else if (currentWeek + 2 == event.WeekNumber) {
            week = context.getString(R.string.two_weeks_later);
        } else if (event.WeekNumber > currentWeek) {
            week = (event.WeekNumber - currentWeek) + " " + context.getString(R.string.weeks_later);
        } else if (event.WeekNumber < currentWeek) {
            week = context.getString(R.string.passed) + " : " + context.getString(R.string.week) + " " + String.valueOf(event.WeekNumber);
        } else {
            throw new RuntimeException("un excepted week number");
        }

        String day;
        if (currentWeek == event.WeekNumber){
            if (currentDayOfWeek == event.time.dayOfWeek) {
                day = context.getString(R.string.today);
            } else if (currentDayOfWeek + 1 == event.time.dayOfWeek |
                    (currentDayOfWeek == 6 & 0 == event.time.dayOfWeek)) {
                day = context.getString(R.string.tomorrow);
            } else {
                day = getDayString(event.time.dayOfWeek);
            }
        } else {
            day = getDayString(event.time.dayOfWeek);
        }

        return week + " " + day + " " + event.time.toString() + " " + event.what;
    }


    public static String getDayString(int day) {
        return CH.getStringArray(R.array.weekDays)[day];
    }

    public static String NumToString(String s) {
        s.replace("0", CH.getStringArray(R.array.nums)[0]);
        s.replace("1", CH.getStringArray(R.array.nums)[1]);
        s.replace("2", CH.getStringArray(R.array.nums)[2]);
        s.replace("3", CH.getStringArray(R.array.nums)[3]);
        s.replace("4", CH.getStringArray(R.array.nums)[4]);
        s.replace("5", CH.getStringArray(R.array.nums)[5]);
        s.replace("6", CH.getStringArray(R.array.nums)[6]);
        s.replace("7", CH.getStringArray(R.array.nums)[7]);
        s.replace("8", CH.getStringArray(R.array.nums)[8]);
        s.replace("9", CH.getStringArray(R.array.nums)[9]);
        return s;
    }

    public static String NumToString(Number s) {
        return NumToString(String.valueOf(s));
    }


    public static String getWeekNumberString(int weekNumber) {
        if (weekNumber > 38) {
            return CH.getString(R.string.not_set);
        }
        return CH.getString(R.string.week) + " " + NumToString(weekNumber);
    }
}
