package com.github.ali77gh.unitools.data.model;

/**
 * Created by ali77gh on 12/11/18.
 */

public class StoragePack {

    private String Name;
    private int PicCount;
    private int VoiceCount;

    public StoragePack(String name, int picCount, int voiceCount) {
        Name = name;
        PicCount = picCount;
        VoiceCount = voiceCount;
    }

    public String getName() {
        return Name;
    }

    public int getPicCount() {
        return PicCount;
    }

    public int getVoiceCount() {
        return VoiceCount;
    }
}
