package com.github.ali77gh.unitools.core.audio;

import android.media.MediaRecorder;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.AppNotification;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.MyDataBeen;

import java.io.IOException;

/**
 * Created by ali on 10/5/18.
 */

public class VoiceRecorder {

    private static MediaRecorder mediaRecorder;
    private static boolean isRecording = false;
    private static MediaRecorderCallback callback;

    public static void setCallback(MediaRecorderCallback callback) {
        VoiceRecorder.callback = callback;
    }

    public static boolean isRecording() {
        return isRecording;
    }

    public static void Record(String path) {

        MediaRecorderReady(path);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        mediaRecorder.start();
        isRecording = true;
        AppNotification.ShowRecording();
        callback.onStateChanged(isRecording);
        CH.toast(R.string.close_app_from_recent_will_stop_recording, true);
    }

    public static void Stop() {
        mediaRecorder.stop();
        mediaRecorder.release();
        isRecording = false;
        MyDataBeen.onNewVoice();
        AppNotification.HideRecording();
        callback.onStateChanged(isRecording);
    }


    //configs
    private static void MediaRecorderReady(String path) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(path);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    }

    public interface MediaRecorderCallback {
        void onStateChanged(boolean recording);
    }
}
