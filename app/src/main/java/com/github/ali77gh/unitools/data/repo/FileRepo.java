package com.github.ali77gh.unitools.data.repo;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.easyrepolib.KeyValDb;
import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.repos.BitmapRepo;
import com.example.easyrepolib.repos.StringRepo;

import java.util.ArrayList;

/**
 * Created by ali on 10/4/18.
 */

public class FileRepo {

    private static KeyValDb db;
    private static BitmapRepo bitmapRepo;
    private static StringRepo stringRepo;

    private static String table = "files";

    public static void init(Context context) {
        db = new KeyValDb(context, table);
        bitmapRepo = new BitmapRepo(context, GRepo.Mode.LOCAL);
        stringRepo = new StringRepo(context, GRepo.Mode.LOCAL);
    }

    public static String Add(Bitmap bitmap, String... tags) {

        //todo return random id
        return null;
    }

    public static Bitmap getBitmapById(String id) {
        return null;
    }

    public static ArrayList<Bitmap> getBitmapByTag(String... tag) {
        return null;
    }

    //todo add audio Load / Save
}
