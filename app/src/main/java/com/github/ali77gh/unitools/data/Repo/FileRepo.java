package com.github.ali77gh.unitools.data.Repo;

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

    private KeyValDb db;
    private BitmapRepo bitmapRepo;
    private StringRepo stringRepo;

    private String table = "files";

    public FileRepo(Context context) {
        db = new KeyValDb(context, table);
        bitmapRepo = new BitmapRepo(context, GRepo.Mode.LOCAL);
        stringRepo = new StringRepo(context, GRepo.Mode.LOCAL);
    }

    public String Add(Bitmap bitmap, String... tags) {

        //todo return random id
    }

    public Bitmap getBitmapById(String id) {

    }

    public ArrayList<Bitmap> getBitmapByTag(String... tag) {

    }

    //todo add audio Load / Save
}
