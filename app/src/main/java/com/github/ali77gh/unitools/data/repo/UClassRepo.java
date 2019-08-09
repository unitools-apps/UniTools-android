package com.github.ali77gh.unitools.data.repo;

import android.content.Context;

import com.example.easyrepolib.KeyValDb;
import com.github.ali77gh.unitools.core.MyDataBeen;
import com.github.ali77gh.unitools.data.model.UClass;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UClassRepo {

    private static KeyValDb db;
    private static String table = "classes";

    public static void init(Context context) {
        db = new KeyValDb(context, table);
    }

    public static List<UClass> getAll() {
        ArrayList<UClass> list = new ArrayList<>();
        for (Object o : db.ReadAllOfType(UClass.class))
            list.add((UClass) o);
        return (list);
    }

    public static void Insert(UClass uClass) {
        uClass.id = UUID.randomUUID().toString();
        db.insert(uClass.id, uClass);
        MyDataBeen.onNewClass();
    }

    public static void Update(UClass uClass) {
        db.Update(uClass.id, uClass);
    }

    public static UClass getById(String id) {
        return (UClass) db.Read(id, UClass.class);
    }


    public static boolean IsEmpty() {
        return db.IsEmpty();
    }

    public static void Remove(String id) {
        db.Remove(id);
    }

    public static void RemoveAll(){
        for (UClass uClass:getAll())
            Remove(uClass.id);
        if (!IsEmpty()) new RuntimeException("remove all not works");
    }
}
