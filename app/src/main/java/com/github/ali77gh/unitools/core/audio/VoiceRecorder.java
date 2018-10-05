package com.github.ali77gh.unitools.core.audio;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import java.io.IOException;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by ali on 10/5/18.
 */

public class VoiceRecorder {

    private String AudioSavePathInDevice;
    private MediaRecorder mediaRecorder;
    private String fileName = "temp";

    private Activity mContext;

    public VoiceRecorder(Activity context) {
        mContext = context;
        AudioSavePathInDevice =
                Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/" +
                        fileName + "AudioRecording.3gp";
    }

    public void Record() throws IOException {

        if (checkPermission()) {

            MediaRecorderReady();

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
            } catch (IllegalStateException e) {
                throw e;
            } catch (IOException e) {
                throw e;
            }
            Toast.makeText(mContext, "Recording started",
                    Toast.LENGTH_LONG).show();
        } else {
            requestPermission();
        }
    }

    public void StopRecord() {
        mediaRecorder.stop();
        Toast.makeText(mContext, "Recording Completed", Toast.LENGTH_LONG).show();
    }

    public void requestPermission() {
        ActivityCompat.requestPermissions(mContext, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, 1);
    }

    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(mContext,
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(mContext,
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void MediaRecorderReady() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }


}
