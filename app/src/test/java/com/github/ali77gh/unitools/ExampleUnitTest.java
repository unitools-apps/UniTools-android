package com.github.ali77gh.unitools;

import com.github.ali77gh.unitools.core.ShortIdGenerator;
import com.github.ali77gh.unitools.data.Model.Friend;
import com.github.ali77gh.unitools.data.Model.Time;
import com.github.ali77gh.unitools.data.Model.UClass;
import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        Log(ShortIdGenerator.Generate(8));

        UClass uClass = new UClass();
        uClass.id = "fsalkfvslmv";
        uClass.dayOfWeek = 3;
        uClass.time = new Time(12,30);
        uClass.what = "شیمی";
        uClass.where = "b110";

        Friend friend = new Friend();
        friend.name = "ali";
        friend.classList = new ArrayList<>();
        friend.classList.add(uClass);
        friend.classList.add(uClass);
        friend.classList.add(uClass);
        friend.classList.add(uClass);
        friend.classList.add(uClass);
        friend.classList.add(uClass);
        friend.classList.add(uClass);
        friend.classList.add(uClass);
        Log(new Gson().toJson(friend));
    }

    private void Log(Object o) {
        System.out.println(o);
    }
}