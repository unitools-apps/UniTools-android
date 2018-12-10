package com.github.ali77gh.unitools;

import com.github.ali77gh.unitools.data.model.UClass;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    int[] a = new int[10];
    int[] b = new int[10];

    @Test
    public void test() {

        List<Integer> moshtarak = new ArrayList<>();

        System.out.println("list1:");
        for (int i = 0; i < 10; i++) {
            a[i] = getRandomNumber();
            System.out.print(a[i] + " ");
        }

        System.out.println("\nlist2:");
        for (int i = 0; i < 10; i++) {
            b[i] = getRandomNumber();
            System.out.print(b[i] + " ");
        }

        System.out.println("\nmoshtarak:");
        for (int i = 0; i < 9; i++) {
            boolean x = isExistInList2(a[i]);
            if (x) {
                if (moshtarak.indexOf(a[i])==-1){
                    moshtarak.add(a[i]);
                    System.out.print(a[i] + " ");
                }
            }
        }


    }

    private boolean isExistInList2(int val) {
        for (int i : b) {
            if (i == val) return true;
        }
        return false;
    }

    private static int getRandomNumber() {
        Random r = new Random();
        return r.nextInt(9) + 1;
    }

    private void Log(Object o) {
        System.out.println(o);
    }

    private void Log(UClass u) {
        Log(u.id);
    }

    private void LogList(List<UClass> list) {
        for (UClass o : list) Log(o);
    }


}