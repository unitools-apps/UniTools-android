package com.github.ali77gh.unitools.core.audio;

import android.app.Activity;
import android.media.MediaRecorder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

/**
 * Created by ali on 10/5/18.
 */

public class VoiceRecorder {

    private String TempAudioPath;
    private MediaRecorder mediaRecorder;
    private String fileName = "temp";

    private Activity mActivity;

    public VoiceRecorder(Activity activity) {
        mActivity = activity;
        TempAudioPath = activity.getFilesDir() + "/" + fileName + ".3gp";
    }

    public void Record() {

        if (checkPermission()) {

            MediaRecorderReady();
            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
            mediaRecorder.start();

        } else {
            requestPermission();
        }
    }

    public void stop() {
        mediaRecorder.stop();
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(mActivity, new String[]{RECORD_AUDIO}, 1);
    }

    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(mActivity, RECORD_AUDIO) == PERMISSION_GRANTED;
    }

    //configs
    private void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(TempAudioPath);
    }

    public void SaveTempAs(String id) {

        File dir = mActivity.getFilesDir();
        if (dir.exists()) {
            File from = new File(dir, "temp.3gp");
            File to = new File(dir, id);

            //debug
            Log.d("files", dir.getPath());
            for (String child : dir.list())
                Log.d("files", child);

            if (from.exists())
                from.renameTo(to);
            else Log.d("files", "from not found");
        } else
            Log.d("files", "dir not found");
    }
}
