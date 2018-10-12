package com.github.ali77gh.unitools.core;

import android.content.Context;

/**
 * Created by ali77gh on 10/12/18.
 */

public class ContextHolder {

    private static Context AppContext;

    public static void init(Context context){
        AppContext = context;
    }

    public static Context getAppContext(){
        return AppContext;
    }
}
