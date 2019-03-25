package com.github.ali77gh.unitools.core;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.data.model.BackupModel;
import com.github.ali77gh.unitools.data.model.Event;
import com.github.ali77gh.unitools.data.model.Friend;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.repo.EventRepo;
import com.github.ali77gh.unitools.data.repo.FriendRepo;
import com.github.ali77gh.unitools.data.repo.UClassRepo;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class Backup {

    public static String backupFilePath = FilePackProvider.AppPath + "/" + "backup.json";

    public static void DoBackup(boolean classes , boolean events , boolean friends){

        //make savable object
        List<UClass> classesList = new ArrayList<>();
        List<Event> eventsList = new ArrayList<>();
        List<Friend> friendsList = new ArrayList<>();

        if (classes) classesList = UClassRepo.getAll();
        if (events) eventsList = EventRepo.getAll();
        if (friends) friendsList = FriendRepo.getAll();

        BackupModel bm = new BackupModel(
                CH.getString(R.string.app_version),
                classesList,
                eventsList,
                friendsList
        );

        //write to file
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(new File(backupFilePath)));
            outputStreamWriter.write(new Gson().toJson(bm));
            outputStreamWriter.close();
        }catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void Restore(boolean classes , boolean events , boolean friends){
        try {
            FileInputStream inputStream = new FileInputStream(new File(backupFilePath));

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString;
            StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();

            BackupModel bm = new Gson().fromJson(stringBuilder.toString(),BackupModel.class);

            if (classes)
            for (UClass uClass : bm.classes){
                UClassRepo.Insert(uClass);
            }

            if (events)
            for (Event event : bm.events){
                EventRepo.Insert(event);
            }

            if (friends)
            for (Friend friend : bm.friends){
                FriendRepo.Insert(friend);
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static void ClearCurrentAndRestore(boolean classes , boolean events , boolean friends){

         if(classes) UClassRepo.RemoveAll();
         if(events) EventRepo.RemoveAll();
         if(friends) FriendRepo.RemoveAll();
         Restore(classes,events,friends);
    }

    public static boolean IsBackupPossible(){

        if (FriendRepo.getAll().size() != 0) return true;
        if (EventRepo.getAll().size() != 0) return true;
        return UClassRepo.getAll().size() != 0;
    }

    public static boolean IsRestorePossible(){
        File backupFile = new File(backupFilePath);
        return backupFile.exists();
    }

}
