package com.github.ali77gh.unitools.core.tools;

import com.github.ali77gh.unitools.data.Model.Time;

import java.util.Date;

/**
 * Created by ali on 10/4/18.
 */

public class DateTimeTools {

    public static int getCurrentDayOfWeek() {
        int res= new Date().getDay() + 1;
        if (res==7) res =0;
        return res ;
    }

    /**
     * like unix time but days
     */
    private static int getCurrentDays() {
        long unixTime = new Date().getTime();

        unixTime = unixTime / 1000;

        int unixTimeMin = (int) (unixTime / 60);

        return unixTimeMin / (24 * 60);

    }

    public static Time getCurrentTime() {
        Date d = new Date();
        return new Time(getCurrentDayOfWeek(), d.getHours(), d.getMinutes());
    }

    public static class WeekTools {

        /**
         * @param weekNumber current week number
         */
        public static int getFirstDayOfUni(int weekNumber) {

            int nowDAYS = getCurrentDays();
            int firstDayOfWeekDAYS = nowDAYS - getCurrentTime().dayOfWeek;

            return firstDayOfWeekDAYS - (weekNumber * 7);
        }

        public static int getWeekNumber(int firstDay) {

            return (getCurrentDays() - firstDay) / 7;
        }

    }
}
