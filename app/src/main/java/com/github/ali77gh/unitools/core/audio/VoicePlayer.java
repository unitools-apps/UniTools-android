package com.github.ali77gh.unitools.core.audio;

import android.app.Activity;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by ali on 10/5/18.
 */

public class VoicePlayer {

    private MediaPlayer mediaPlayer;
    private Activity mActivity;

    public VoicePlayer(Activity activity) {
        mActivity = activity;
    }

    public void play(String name) {

        String AudioPath = mActivity.getFilesDir() + "/" + name ;

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(AudioPath);
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        mediaPlayer.start();
    }

    public void Stop() {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
}
