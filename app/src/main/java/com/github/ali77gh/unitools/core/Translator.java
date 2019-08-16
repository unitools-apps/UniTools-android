package com.github.ali77gh.unitools.core;

import com.ali.uneversaldatetools.date.GregorianDateTime;
import com.ali.uneversaldatetools.date.IDate;
import com.ali.uneversaldatetools.date.TimeZoneHelper;
import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.data.model.Event;
import com.github.ali77gh.unitools.data.model.UClass;

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
                toClass = "(" + ToNextClassString(uClass.time.getMins() - DateTimeTools.getCurrentTime().getMins()) + " " + CH.getString(R.string.later) + " )";
            } else
                day = CH.getString(R.string.today);

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

        GregorianDateTime today = GregorianDateTime.Now();
        GregorianDateTime eventDay = new GregorianDateTime((int) event.unixTime, TimeZoneHelper.getSystemTimeZone());

        int diffInDays = eventDay.getDays() - today.getDays();

        if (diffInDays > 0) {

            return String.valueOf(diffInDays) +
                    " " +
                    CH.getString(R.string.day) +
                    " " +
                    CH.getString(R.string.after) +
                    " " +
                    NumToStringClockMode(new GregorianDateTime((int) event.unixTime, TimeZoneHelper.getSystemTimeZone()).getHour()) +
                    ":" +
                    NumToStringClockMode(new GregorianDateTime((int) event.unixTime, TimeZoneHelper.getSystemTimeZone()).getMin()) +
                    " " +
                    event.what;

        } else if (diffInDays < 0) {
            diffInDays *= -1; //negetive to postive
            return String.valueOf(diffInDays) +
                    " " +
                    CH.getString(R.string.day) +
                    " " +
                    CH.getString(R.string.ago) +
                    " " +
                    NumToStringClockMode(new GregorianDateTime((int) event.unixTime, TimeZoneHelper.getSystemTimeZone()).getHour()) +
                    ":" +
                    NumToStringClockMode(new GregorianDateTime((int) event.unixTime, TimeZoneHelper.getSystemTimeZone()).getMin()) +
                    " " +
                    event.what;
        } else {
            //today
            return CH.getString(R.string.today) +
                    " " +
                    NumToStringClockMode(new GregorianDateTime((int) event.unixTime, TimeZoneHelper.getSystemTimeZone()).getHour()) +
                    ":" +
                    NumToStringClockMode(new GregorianDateTime((int) event.unixTime, TimeZoneHelper.getSystemTimeZone()).getMin()) +
                    " " +
                    event.what;
        }


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

    public static String NumToStringClockMode(int value){
        if (value < 10)
            return  "0" + String.valueOf(value);
        else
            return String.valueOf(value);
    }

    private static String ToNextClassString(int mins){

        final int hour = mins / 60;
        final int min = mins % 60;

        return NumToStringClockMode(hour) + ":" + NumToStringClockMode(min);
    }


    public static String getWeekNumberString(int weekNumber) {
        if (weekNumber > 38) {
            return CH.getString(R.string.not_set);
        }
        return CH.getString(R.string.week) + " " + NumToString(weekNumber);
    }

    public static String getDateString(IDate date) {
        return String.format("%04d/%02d/%02d ", date.getYear(), date.getMonth(), date.getDay());
    }
}
