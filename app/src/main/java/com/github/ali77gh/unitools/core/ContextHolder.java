package com.github.ali77gh.unitools.core;

import android.content.Context;

import com.github.ali77gh.unitools.data.repo.EventRepo;
import com.github.ali77gh.unitools.data.repo.FileRepo;
import com.github.ali77gh.unitools.data.repo.FriendRepo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

/**
 * Created by ali77gh on 10/12/18.
 */

public class ContextHolder {

    private static Context AppContext;


    public static Context getAppContext(){
        return AppContext;
    }

    public static void initStatics(Context context) {
        EventRepo.init(context);
        FileRepo.init(context);
        FriendRepo.init(context);
        UserInfoRepo.init(context);
        AppContext = context;
    }
}
