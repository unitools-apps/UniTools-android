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
    public static final String PDF_PATH_NAME = "pdf";

    private static List<FilePack> _filePacks;
    public static final String AppPath = Environment.getExternalStorageDirectory() + File.separator + "UniTools";

    public static void Init() {

        File dir = new File(AppPath);
        if (!dir.exists())
            dir.mkdir();

        File[] subDirs = dir.listFiles();
        if (subDirs == null) return; // permission not granted

        Sort(subDirs);

        _filePacks = new ArrayList<>();
        for (File f : subDirs) {
            String name = f.getPath().substring(f.getPath().lastIndexOf(File.separator) + 1);
            try {
                int imageCount = new File(f.getPath() + File.separator + IMAGE_PATH_NAME).listFiles().length;
                int voiceCount = new File(f.getPath() + File.separator + VOICE_PATH_NAME).listFiles().length;
                int pdfCount = new File(f.getPath() + File.separator + PDF_PATH_NAME).listFiles().length;
                _filePacks.add(new FilePack(name, imageCount, voiceCount, pdfCount));
            } catch (NullPointerException e) {
                // ignore unstandard folders
            }
        }
    }

    public static void Sort(File[] files) {
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
        return AppPath + File.separator + name;
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
        File rootDir = new File(AppPath + File.separator + name);
        File voiceDir = new File(AppPath + File.separator + name + File.separator + VOICE_PATH_NAME);
        File imageDir = new File(AppPath + File.separator + name + File.separator + IMAGE_PATH_NAME);
        File notesDir = new File(AppPath + File.separator + name + File.separator + NOTES_PATH_NAME);
        File pdfDir = new File(AppPath + File.separator + name + File.separator + PDF_PATH_NAME);

        rootDir.mkdir();
        voiceDir.mkdir();
        imageDir.mkdir();
        notesDir.mkdir();
        pdfDir.mkdir();
        Init();
    }

    public static void RenameFilePack(String from, String to) {
        File dirFrom = new File(AppPath + File.separator + from);
        File dirTo = new File(AppPath + File.separator + to);
        dirFrom.renameTo(dirTo);

        for (FilePack i : _filePacks) {
            if (i.getName().equals(from)) {
                i.Name = to;
                break;
            }
        }
    }

    public static void DeleteFilePack(String name) {
        File rootDir = new File(AppPath + File.separator + name);
        DeleteDir(rootDir);

        for (FilePack i : _filePacks) {
            if (i.getName().equals(name)) {
                _filePacks.remove(i);
                break;
            }
        }
    }

    private static void DeleteDir(File file) {

        for (File childFile : file.listFiles()) {

            if (childFile.isDirectory()) {
                DeleteDir(childFile);
            } else {
                childFile.delete();
            }
        }
        file.delete();
    }

    public static String getMaxPicCode(final String path) {
        File picsDir = new File(path + File.separator + FilePackProvider.IMAGE_PATH_NAME);

        if (!picsDir.isDirectory()) throw new RuntimeException("is not dir");

        String max = "0";
        for (File f : picsDir.listFiles()) {
            if (Integer.valueOf(f.getName().substring(0,f.getName().indexOf("."))) > Integer.valueOf(max))
                max = f.getName().substring(0,f.getName().indexOf("."));
        }
        return String.valueOf(Integer.valueOf(max) + 1);
    }

    public static String getMaxVoiceCode(final String path) {
        File picsDir = new File(path + File.separator + FilePackProvider.VOICE_PATH_NAME);

        if (!picsDir.isDirectory()) throw new RuntimeException("is not dir");

        String max = "0";
        for (File f : picsDir.listFiles()) {
            if (Integer.valueOf(f.getName().substring(0,f.getName().indexOf("."))) > Integer.valueOf(max))
                max = f.getName().substring(0,f.getName().indexOf("."));
        }
        return String.valueOf(Integer.valueOf(max) + 1);
    }
}