package com.github.ali77gh.unitools.core.onlineapi;

import android.provider.Settings;

import com.github.ali77gh.unitools.core.ContextHolder;

/**
 * Created by ali77gh on 1/15/19.
 */

class AndroidId {

    static String get(){
        return Settings.Secure.getString(ContextHolder.getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
