package com.github.ali77gh.unitools.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 10/4/18.
 */

public class Friend {

    public String id;
    public String name;
    public List<UClass> classList;

    //for qr code optimize
    public class MinimalFriend{

        public MinimalFriend(List<UClass> classes){
            classList = new ArrayList<>();
            for (UClass i : classes){
                classList.add(i.getMinimal());
            }
        }

        public List<UClass.MinimalUClass> classList;
    }

    public MinimalFriend getMinimal(){
        return new MinimalFriend(classList);
    }

    public static Friend MinimalToFull(MinimalFriend minimalFriend){
        Friend friend = new Friend();
        friend.id="";
        friend.name="";
        List<UClass> classes = new ArrayList<>();

        for(UClass.MinimalUClass uClass:minimalFriend.classList){
            classes.add(new UClass(uClass.where,uClass.what,uClass.time));
        }
        friend.classList = classes;
        return friend;
    }
}
