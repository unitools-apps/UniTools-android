package com.github.ali77gh.unitools.core.alarm;

import android.content.Context;

import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.core.audio.SilentManager;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.core.tools.Sort;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.model.UserInfo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

import java.util.List;

/**
 * Created by ali77gh on 11/25/18.
 */

class Silent {

    private static int classCount = 120; // 2 hours

    static void on15Min(Context context) {
        ContextHolder.initStatics(context);
        List<UClass> classes = UserInfoRepo.getUserInfo().Classes;
        Sort.SortClass(classes);
        silent(context, classes.get(0));
    }

    private static void silent(Context context, UClass nextClass) {

        UserInfo ui = UserInfoRepo.getUserInfo();
        if (!ui.AutoSilent) return;

        int def = nextClass.time.getMins() - DateTimeTools.getCurrentTime().getMins();
        if (def > classCount || def < 0) {
            SilentManager.Silent(context);
        } else {
            SilentManager.Normal(context);
        }
    }
}
