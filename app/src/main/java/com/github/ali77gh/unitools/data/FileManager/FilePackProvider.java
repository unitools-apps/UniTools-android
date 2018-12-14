package com.github.ali77gh.unitools.data.FileManager;

import android.os.Environment;

import com.github.ali77gh.unitools.data.model.FilePack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali77gh on 12/12/18.
 */

public class FilePackProvider {

    public static final String VOICE_PATH_NAME = "voices";
    public static final String IMAGE_PATH_NAME = "images";
    public static final String NOTES_PATH_NAME = "notes";

    private static List<FilePack> _filePacks;
    private static final String _appPath = Environment.getExternalStorageDirectory() + File.separator + "UniTools";

    public static void Init() {

        File dir = new File(_appPath);
        if (!dir.exists())
            dir.mkdir();

        File[] subDirs = dir.listFiles();
        if (subDirs == null) return; // permission not granted

        Sort(subDirs);

        _filePacks = new ArrayList<>();
        for (File f : subDirs) {
            String name = f.getPath().substring(f.getPath().lastIndexOf(File.separator) + 1, f.getPath().length());
            try {
                int imageCount = new File(f.getPath() + File.separator + IMAGE_PATH_NAME).listFiles().length;
                int voiceCount = new File(f.getPath() + File.separator + VOICE_PATH_NAME).listFiles().length;
                _filePacks.add(new FilePack(name, imageCount, voiceCount));
            } catch (NullPointerException e) {
                // ignore unstandard folders
            }
        }
    }

    private static void Sort(File[] files) {
        File temp;
        for (int i = 0; i < files.length - 1; i++) {
            for (int j = i + 1; j < files.length; j++) {
                if (files[i].lastModified() < files[j].lastModified()) {
                    temp = files[i];
                    files[i] = files[j];
                    files[j] = temp;
                }
            }
        }
    }

    public static String getPathOfPack(String name) {
        return _appPath + File.separator + name;
    }

    public static List<FilePack> getFilePacks() {
        return _filePacks;
    }

    public static List<String> getFilePacksNames() {
        List<String> names = new ArrayList<>();
        for (FilePack FP : getFilePacks()) {
            names.add(FP.getName());
        }
        return names;
    }

    public static void CreateFilePack(String name) {
        File rootDir = new File(_appPath + File.separator + name);
        File voiceDir = new File(_appPath + File.separator + name + File.separator + VOICE_PATH_NAME);
        File imageDir = new File(_appPath + File.separator + name + File.separator + IMAGE_PATH_NAME);
        File notesDir = new File(_appPath + File.separator + name + File.separator + NOTES_PATH_NAME);

        rootDir.mkdir();
        voiceDir.mkdir();
        imageDir.mkdir();
        notesDir.mkdir();
        Init();
    }
}
