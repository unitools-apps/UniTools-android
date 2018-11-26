package com.github.ali77gh.unitools.data.repo;

import android.content.Context;

import com.example.easyrepolib.KeyValDb;
import com.github.ali77gh.unitools.core.ShortIdGenerator;
import com.github.ali77gh.unitools.data.model.Friend;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 10/4/18.
 */

public class FriendRepo {

    private static KeyValDb db;
    private static String table = "friends";

    public static void init(Context context) {
        db = new KeyValDb(context, table);
    }

    public static List<Friend> getAll() {
        ArrayList<Friend> list = new ArrayList<>();
        for (Object o : db.ReadAllOfType(Friend.class))
            list.add((Friend) o);
        return (list);
    }

    public static void insert(Friend newFriend) {
        newFriend.id = ShortIdGenerator.Generate(5);
        db.insert(newFriend.id, newFriend);
    }

    public static Friend getById(String id) {
        return (Friend) db.Read(id, Friend.class);
    }


    public static boolean IsEmpty() {
        return db.IsEmpty();
    }

    public static void Remove(String id) {
        db.Remove(id);
    }
}
