package com.github.ali77gh.unitools.data.model;

/**
 * Created by ali on 10/4/18.
 */

public class Time {

    public int dayOfWeek; // 0 -> 6
    public int hour;
    public int min;

    public Time(int dayOfWeek, int hour, int min) {
        if (!Validator(hour, min))
            throw new IllegalArgumentException("time is not valid" + hour + ":" + min);
        this.dayOfWeek = dayOfWeek;
        this.hour = hour;
        this.min = min;
    }

    /**
     * @return minutes from last minute of last week
     */
    public int getMins() {
        return min + (hour * 60) + (dayOfWeek * 1440);
    }

    public static boolean Validator(int hour, int min) {
        if (hour > 24) return false;
        if (min > 60) return false;
        if (hour < 0) return false;
        if (min < 0) return false;
        return true;
    }

    public String toString() {
        String hour = String.valueOf(this.hour);
        String min = String.valueOf(this.min);

        if (hour.length() == 1) hour = "0" + hour;
        if (min.length() == 1) min = "0" + min;
        return hour + ":" + min;
    }
}
