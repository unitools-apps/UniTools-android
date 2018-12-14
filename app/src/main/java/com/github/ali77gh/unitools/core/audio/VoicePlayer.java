package com.github.ali77gh.unitools.core.audio;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by ali on 10/5/18.
 */

public class VoicePlayer {

    private MediaPlayer mediaPlayer;

    public void Play(String path) {

        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        mediaPlayer.start();
    }

    public boolean IsPlaying() {
        if (mediaPlayer == null) return false;
        return mediaPlayer.isPlaying();
    }

    public void Stop() {

        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
