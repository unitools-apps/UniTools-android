package com.github.ali77gh.unitools.data.model;

/**
 * Created by ali on 10/10/18.
 */

public class Event {

    public String what;
    public String describe = "";
    public String id;
    public long unixTime;

    public Event(String what, long time, String describe) {
        this.what = what;
        this.unixTime = time;
        this.describe = describe;
    }

    public Event() {
    }
}
