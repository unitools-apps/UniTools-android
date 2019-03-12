package com.github.ali77gh.unitools.core.tools;

import com.github.ali77gh.unitools.data.model.Time;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

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

    public static int UnixTimeToWeek(int unixTime) {
        int unixTimeMin = unixTime / 60;
        int days = unixTimeMin / (24 * 60);
        int daysFromFirstDayOfUni = days - UserInfoRepo.getUserInfo().FirstDayOfUni;

        return (daysFromFirstDayOfUni / 7);
    }

    public static Time getCurrentTime() {
        Date d = new Date();
        return new Time(getCurrentDayOfWeek(), d.getHours(), d.getMinutes());
    }

    public static class WeekTools {

        /**
         * like unix time but days
         */
        private static int getCurrentDays() {
            long unixTime = new Date().getTime();

            unixTime /= 1000;

            int unixTimeMin = (int) (unixTime / 60);

            return unixTimeMin / (24 * 60);

        }

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
