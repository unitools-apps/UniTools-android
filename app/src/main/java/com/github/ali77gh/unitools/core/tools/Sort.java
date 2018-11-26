package com.github.ali77gh.unitools.core.tools;

import com.github.ali77gh.unitools.data.model.Event;
import com.github.ali77gh.unitools.data.model.Friend;
import com.github.ali77gh.unitools.data.model.Time;
import com.github.ali77gh.unitools.data.model.UClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali77gh on 10/24/18.
 */

public class Sort {

    //tested
    public static void SortClass(List<UClass> classes) {


        Time now = DateTimeTools.getCurrentTime();

        ExchangeSortClass(classes);

        //shift
        int nextClassIndex = 0;
        for (UClass u : classes) {
            if (u.time.getMins() > now.getMins()) {
                nextClassIndex = classes.indexOf(u);
                break;
            }
        }
        List<UClass> pre = new ArrayList<>(classes.subList(0, nextClassIndex));
        List<UClass> past = new ArrayList<>(classes.subList(nextClassIndex, classes.size()));

        //todo find better way to add two lists
        classes.clear();
        classes.addAll(past);
        classes.addAll(pre);
    }

    public static void SortFriend(List<Friend> friends) {

    }

    public static void SortEvent(List<Event> events) {

        Time now = DateTimeTools.getCurrentTime();

        ExchangeSortEvent(events);

        //shift
        int nextClassIndex = 0;
        for (Event u : events) {
            if (u.time.getMins() > now.getMins()) {
                nextClassIndex = events.indexOf(u);
                break;
            }
        }
        List<Event> pre = new ArrayList<>(events.subList(0, nextClassIndex));
        List<Event> past = new ArrayList<>(events.subList(nextClassIndex, events.size()));

        //todo find better way to add two lists
        events.clear();
        for (Event u : past)
            events.add(u);
        for (Event u : pre)
            events.add(u);
    }

    private static void ExchangeSortClass(List<UClass> uClasses) {
        int i, j;
        UClass temp;
        for (i = 0; i < uClasses.size() - 1; i++) {
            for (j = i + 1; j < uClasses.size(); j++) {
                if (uClasses.get(i).time.getMins() > uClasses.get(j).time.getMins()) {
                    temp = uClasses.get(i);
                    uClasses.set(i, uClasses.get(j));
                    uClasses.set(j, temp);
                }
            }
        }
    }

    private static void ExchangeSortEvent(List<Event> events) {
        int i, j;
        Event temp;
        for (i = 0; i < events.size() - 1; i++) {
            for (j = i + 1; j < events.size(); j++) {
                if (events.get(i).WeekNumber > events.get(j).WeekNumber | events.get(i).time.getMins() > events.get(j).time.getMins()) {
                    temp = events.get(i);
                    events.set(i, events.get(j));
                    events.set(j, temp);
                }
            }
        }
    }
}
