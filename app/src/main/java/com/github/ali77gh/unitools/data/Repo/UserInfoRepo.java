package com.github.ali77gh.unitools.data.Repo;

import android.content.Context;

import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.repos.ObjectRepo;
import com.github.ali77gh.unitools.data.Model.UClass;
import com.github.ali77gh.unitools.data.Model.UserInfo;

import java.util.ArrayList;

/**
 * Created by ali on 10/4/18.
 */

public class UserInfoRepo {

    private ObjectRepo objectRepo;
    private String id = "user_config";

    public UserInfoRepo(Context context) {
        objectRepo = new ObjectRepo(context, GRepo.Mode.LOCAL);
    }

    public boolean IsExist() {
        return objectRepo.Load(id, UserInfo.class) != null;

    }

    public UserInfo getUserInfo() {
        return (UserInfo) objectRepo.Load(id, UserInfo.class);
    }

    public void setUserInfo(UserInfo userInfo) {
        objectRepo.Save(id, userInfo);
    }

    public void Drop() {
        objectRepo.Remove(id);
    }

    public void AddClass(UClass uClass) {
        UserInfo ui = getUserInfo();

        ui.classes.add(uClass);
        setUserInfo(ui);
    }

    public void RemoveClass(String id) {

        UserInfo ui = getUserInfo();
        int index = -1;
        //find
        for (UClass uClass : ui.classes) {
            if (uClass.id.equals(id)) ;
            index = ui.classes.indexOf(uClass);
        }
        if (index == -1) throw new RuntimeException("class not found on RemoveClass");
        //remove
        ui.classes.remove(index);
        setUserInfo(ui);
    }

    public void RemoveAllClasses() {

        UserInfo ui = getUserInfo();
        ui.classes = new ArrayList<>();
        setUserInfo(ui);
    }


}
