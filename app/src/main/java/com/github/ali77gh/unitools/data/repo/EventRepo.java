package com.github.ali77gh.unitools.data.repo;

import android.content.Context;

import com.example.easyrepolib.KeyValDb;
import com.github.ali77gh.unitools.core.MyDataBeen;
import com.github.ali77gh.unitools.data.model.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ali on 10/10/18.
 */

public class EventRepo {

    private static KeyValDb db;
    private static String table = "events";

    public static void init(Context context) {
        db = new KeyValDb(context, table);
    }

    public static List<Event> getAll() {
        ArrayList<Event> list = new ArrayList<>();
        for (Object o : db.ReadAllOfType(Event.class))
            list.add((Event) o);
        return (list);
    }

    public static void Insert(Event newEvent) {
        newEvent.id = UUID.randomUUID().toString();
        db.insert(newEvent.id, newEvent);
        MyDataBeen.onNewEvent();
    }

    public static Event getById(String id) {
        return (Event) db.Read(id, Event.class);
    }

    public static void Update(Event event){
        db.Update(event.id,event);
    }

    public static boolean IsEmpty() {
        return db.IsEmpty();
    }

    public static void Remove(String id) {
        db.Remove(id);
    }

    public static void RemoveAll(){
        for (Event event:getAll())
            Remove(event.id);
        if (!IsEmpty()) new RuntimeException("remove all not works");
    }
}
