package com.github.ali77gh.unitools.data.repo;

import android.content.Context;

import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.repos.ObjectRepo;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.model.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by ali on 10/4/18.
 */

public class UserInfoRepo {

    private static ObjectRepo objectRepo;
    private static String id = "user_config";

    public static void init(Context context) {
        objectRepo = new ObjectRepo(context, GRepo.Mode.LOCAL);
    }

    public static boolean IsExist() {
        return objectRepo.Load(id, UserInfo.class) != null;

    }

    public static UserInfo getUserInfo() {
        return (UserInfo) objectRepo.Load(id, UserInfo.class);
    }

    public static void setUserInfo(UserInfo userInfo) {
        objectRepo.Save(id, userInfo);
    }

    public static void Drop() {
        objectRepo.Remove(id);
    }

    //classes

    public static void setWeekNumber(int weekNumber) {
        UserInfo ui = getUserInfo();
        ui.FirstDayOfUni = DateTimeTools.WeekTools.getFirstDayOfUni(weekNumber);
        setUserInfo(ui);
    }

    public static int getWeekNumber() {
        return DateTimeTools.WeekTools.getWeekNumber(getUserInfo().FirstDayOfUni);
    }
}
