package com.github.ali77gh.unitools.data.model;

import static com.github.ali77gh.unitools.data.model.UClass.ReminderValues.DISABLE_REMINDER;

/**
 * Created by ali on 10/4/18.
 */

public class UClass {

    public static class ReminderValues{

        public static final int DISABLE_REMINDER = -1;

        public static final int REMINDER_15_MIN = 15 * 60 * 1000;
        public static final int REMINDER_30_MIN = 30 * 60 * 1000;
        public static final int REMINDER_1_HOUR = 60 * 60 * 1000;
        public static final int REMINDER_2_HOUR = 2 * 60 * 60 * 1000;
        public static final int REMINDER_3_HOUR = 3 * 60 * 60 * 1000;
        public static final int REMINDER_4_HOUR = 4 * 60 * 60 * 1000;

    }

    public String id;
    public String where;
    public String what;
    public Time time;
    public int absence;
    public int reminder;//how match before class in millis
    public String teacherName;

    public UClass() {
        reminder = DISABLE_REMINDER;
    }

    public UClass(String where, String what, Time time) {
        this.where = where;
        this.what = what;
        this.time = time;
    }


    // for qr code optimize
    public class MinimalUClass {

        public MinimalUClass(String where, String what, Time time) {
            this.where = where;
            this.what = what;
            this.time = time;
        }

        public String where;
        public String what;
        public Time time;
    }

    public MinimalUClass getMinimal() {
        return new MinimalUClass(where, what, time);
    }
}
