package com.github.ali77gh.unitools.data.model;

/**
 * Created by ali on 10/4/18.
 */

public class UClass {

    public String id;
    public String where;
    public String what;
    public Time time;
    public int apcent;

    public UClass(String where, String what, Time time) {
        this.where = where;
        this.what = what;
        this.time = time;
    }

    public UClass() {
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
