package com.github.ali77gh.unitools.data.Repo;

import android.content.Context;

import com.example.easyrepolib.abstracts.GRepo;
import com.example.easyrepolib.repos.ObjectRepo;
import com.github.ali77gh.unitools.data.Model.UserInfo;

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

    public void UpdateUserInfo(UserInfo userInfo) {
        objectRepo.Save(id, userInfo);
    }

    public void Drop() {
        objectRepo.Remove(id);
    }
}
