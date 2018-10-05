package com.github.ali77gh.unitools.core.audio;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by ali on 10/5/18.
 */

public class VoicePlayer {

    private MediaPlayer mediaPlayer;

    String AudioSavePathInDevice;
    private String fileName = "temp";

    public VoicePlayer(Activity activity){
        AudioSavePathInDevice =
                Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/" +
                        fileName + "AudioRecording.3gp";
    }

    public void Play() {

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(AudioSavePathInDevice);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mediaPlayer.start();
    }

    public void StopPlay(){

        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
