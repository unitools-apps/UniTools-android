package com.github.ali77gh.unitools.data.model;

import java.util.List;

public class BackupModel {

    public String version;
    public List<UClass> classes;
    public List<Event> events;
    public List<Friend> friends;

    public BackupModel(String version,List<UClass> classes , List<Event> events,List<Friend> friends){
        this.version = version;
        this.classes = classes;
        this.events = events;
        this.friends = friends;
    }

}
