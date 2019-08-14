package com.github.ali77gh.unitools.core;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.ArrayRes;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;

import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.data.repo.EventRepo;
import com.github.ali77gh.unitools.data.repo.FriendRepo;
import com.github.ali77gh.unitools.data.repo.UClassRepo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

/**
 * Created by ali77gh on 10/12/18.
 */

/**
 * Context Holder
 */
public class CH {

    private static Context AppContext;

    public static Context getAppContext(){
        return AppContext;
    }

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

    public static void initStatics(Context context) {
        UClassRepo.init(context);
        EventRepo.init(context);
        FriendRepo.init(context);
        FilePackProvider.Init();

        UserInfoRepo.init(context);
        AppContext = context;
    }
}
