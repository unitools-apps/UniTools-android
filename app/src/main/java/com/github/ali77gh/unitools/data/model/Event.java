package com.github.ali77gh.unitools.data.model;

/**
 * Created by ali on 10/10/18.
 */

public class Event {

    public String what;
    public String describe = "";
    public Time time;
    public String id;
    public int WeekNumber;

    public Event(String what, Time time, int weekNumber, String describe) {
        this.what = what;
        this.time = time;
        this.WeekNumber = weekNumber;
        this.describe = describe;
    }

    public Event() {
    }
}
