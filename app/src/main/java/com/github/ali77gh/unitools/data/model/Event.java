package com.github.ali77gh.unitools.data.model;

/**
 * Created by ali on 10/10/18.
 */

public class Event {

    public String what;
    public Time time;
    public String id;
    public int WeekNumber;

    public Event(String what , Time time){
        this.what = what;
        this.time = time;
    }

    public Event(){}
}
