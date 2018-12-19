package com.github.ali77gh.unitools.data.repo;

import android.content.Context;

import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.repos.ObjectRepo;
import com.github.ali77gh.unitools.core.ShortIdGenerator;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.model.UserInfo;

import java.util.ArrayList;
import java.util.List;

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

    public static void AddClass(UClass uClass) {
        UserInfo ui = getUserInfo();
        uClass.id = ShortIdGenerator.Generate(5);
        ui.Classes.add(uClass);
        setUserInfo(ui);
    }

    public static void RemoveClass(String id) {

        UserInfo ui = getUserInfo();
        int index = -1;
        //find
        for (UClass uClass : ui.Classes) {
            if (uClass.id.equals(id)) ;
            index = ui.Classes.indexOf(uClass);
        }
        if (index == -1) throw new RuntimeException("class not found on RemoveClass");
        //remove
        ui.Classes.remove(index);
        setUserInfo(ui);
    }

    public static void UpdateClass(UClass uClass){
        UserInfo userInfo = getUserInfo();
        List<UClass> uClasses =userInfo.Classes;
        for (UClass i : uClasses){
            if (i.id .equals(uClass.id)){
                uClasses.set(uClasses.indexOf(i),uClass);
                break;
            }
        }
        setUserInfo(userInfo);
    }

    public static void RemoveAllClasses() {

        UserInfo ui = getUserInfo();
        ui.Classes = new ArrayList<>();
        setUserInfo(ui);
    }

    public static void setWeekNumber(int weekNumber) {
        UserInfo ui = getUserInfo();
        ui.FirstDayOfUni = DateTimeTools.WeekTools.getFirstDayOfUni(weekNumber);
        setUserInfo(ui);
    }

    public static int getWeekNumber() {
        return DateTimeTools.WeekTools.getWeekNumber(getUserInfo().FirstDayOfUni);
    }
}
