package com.github.ali77gh.unitools.data.model;

/**
 * Created by ali77gh on 12/11/18.
 */

public class FilePack {

    public String Name;
    private int PicCount;
    private int VoiceCount;
    private int PdfCount;

    public FilePack(String name, int picCount, int voiceCount, int pdfCount) {
        Name = name;
        PicCount = picCount;
        VoiceCount = voiceCount;
        PdfCount = pdfCount;
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

    public int getPdfCount() {
        return PdfCount;
    }
}
