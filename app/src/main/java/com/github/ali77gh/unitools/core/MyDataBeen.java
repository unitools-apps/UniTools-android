package com.github.ali77gh.unitools.core;

import android.app.Activity;
import android.content.pm.PackageManager;

import ir.databeen.sdk.Databeen;

//databeen is a user analyse system see : https://my.databeen.ir
public class MyDataBeen {

    private final static String secret = "3836225605c2477c99b753949f2fd09a";

    private static void sendEvent(String name){
        Databeen.sendCustomEventData(name,new String[0], new String[0]);
    }

    //app

    public static void onAppStarts(Activity activity){


        if (haveBazzarInstalled(activity.getPackageManager())){
            Databeen.init(activity, secret, "bazzar");
        }else {
            Databeen.init(activity, secret, "other");
        }

    }

    private static boolean haveBazzarInstalled(PackageManager packageManager) {

        boolean found = true;

        try {

            packageManager.getPackageInfo("com.farsitel.bazaar", 0);
        } catch (PackageManager.NameNotFoundException e) {

            found = false;
        }

        return found;
    }

    public static void onAppStops(){
        Databeen.setDefineExit();
    }

    //home

    public static void onNewClass(){

        sendEvent("new_class");
    }

    public static void onNewFriend(){

        sendEvent("new_friend");
    }

    public static void onNewEvent(){

        sendEvent("new_event");
    }

    public static void onWeekCounterClick(){

        sendEvent("week_counter_clicked");
    }

    public static void onNewAlarm(){

        sendEvent("new_alarm");
    }

    public static void onNewAddFriendWithLink(){

        sendEvent("new_add_friend_with_link");
    }

    public static void onNewAddFriendWithQRCode(){

        sendEvent("new_add_friend_with_QR_code");
    }

    //docs

    public static void onNewDoc(){

        sendEvent("new_doc");
    }

    public static void onNewVoice(){

        sendEvent("new_voice");
    }

    public static void onNewPhoto(){

        sendEvent("new_photo");
    }

    public static void onNewPdfGenerate(){

        sendEvent("new_pdf_generate");
    }

    public static void onNewPdfImport(){

        sendEvent("new_pdf_imported");
    }

    //settings

    public static void onAboutOpened(){

        sendEvent("about_opened");
    }

    public static void onBackup(){

        sendEvent("backup");
    }

    public static void onRestore(){

        sendEvent("restore_backup");
    }

    //back link

    public static void onGithubBackLink(){

        sendEvent("back_link_github");
    }

    public static void onInstagramBackLink(){

        sendEvent("back_link_github");
    }

    public static void onWebsiteBackLink(){

        sendEvent("back_link_github");
    }
}
