package com.github.ali77gh.unitools.core;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.widget.Toast;

import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.data.repo.EventRepo;
import com.github.ali77gh.unitools.data.repo.FriendRepo;
import com.github.ali77gh.unitools.data.repo.UClassRepo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

;

/**
 * Created by ali77gh on 10/12/18.
 */

/**
 * Context Holder
 */
public class CH {

    //heart of class
    private static Context AppContext;

    //call this before use other methods
    public static void initStatics(Context context) {
        UClassRepo.init(context);
        EventRepo.init(context);
        FriendRepo.init(context);
        FilePackProvider.Init();

        UserInfoRepo.init(context);
        AppContext = context;
    }

    //access appContext everywhere
    public static Context getAppContext(){
        return AppContext;
    }

    //resource getters

    public static Resources getResources() {
        return AppContext.getResources();
    }

    public static String getString(@StringRes int id) {
        return getResources().getString(id);
    }

    public static String[] getStringArray(@ArrayRes int id) {
        return getResources().getStringArray(id);
    }

    public static int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }


    //Toasters :)

    public static void toast(String msg, boolean longerTime) {
        if (longerTime)
            Toast.makeText(AppContext, msg, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(AppContext, msg, Toast.LENGTH_SHORT).show();
    }

    public static void toast(String msg) {
        toast(msg, false);
    }

    public static void toast(@StringRes int id, boolean longerTime) {
        toast(getString(id), longerTime);
    }

    public static void toast(@StringRes int id) {
        toast(id, false);
    }

}
