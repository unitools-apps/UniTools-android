package com.github.ali77gh.unitools.core.audio;

import android.media.MediaRecorder;

import java.io.File;
import java.io.IOException;

/**
 * Created by ali on 10/5/18.
 */

public class VoiceRecorder {

    private MediaRecorder mediaRecorder;
    private boolean isRecording = false;

    public boolean isRecording() {
        return isRecording;
    }

    public void Record(String path) {

        MediaRecorderReady(path);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        mediaRecorder.start();
        isRecording = true;

    }

    public void Stop() {
        mediaRecorder.stop();
        mediaRecorder.release();
        isRecording = false;
    }


    //configs
    private void MediaRecorderReady(String path) {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(path);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    }
}
