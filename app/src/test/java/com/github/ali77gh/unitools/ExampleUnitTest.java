package com.github.ali77gh.unitools;

import com.github.ali77gh.unitools.data.Model.UClass;

import org.junit.Test;

import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {


    }

    private void Log(Object o) {
        System.out.println(o);
    }

    private void Log(UClass u){
        Log(u.id);
    }

    private void LogList(List<UClass> list){
        for (UClass o : list) Log(o);
    }
}