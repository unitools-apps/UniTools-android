package com.github.ali77gh.unitools.data.repo;

import android.content.Context;

import com.example.easyrepolib.KeyValDb;
import com.github.ali77gh.unitools.core.MyDataBeen;
import com.github.ali77gh.unitools.data.model.Event;
import com.github.ali77gh.unitools.data.model.Friend;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public static void Insert(Friend newFriend) {
        newFriend.id = UUID.randomUUID().toString();
        db.insert(newFriend.id, newFriend);
        MyDataBeen.onNewFriend();
    }

    public static void Update(Friend friend) {
        db.Update(friend.id, friend);
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

    public static void RemoveAll(){
        for (Friend friend:getAll())
            Remove(friend.id);
        if (!IsEmpty()) new RuntimeException("remove all not works");
    }
}
