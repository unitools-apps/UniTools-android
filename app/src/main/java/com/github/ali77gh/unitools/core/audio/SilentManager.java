package com.github.ali77gh.unitools.core.audio;

import android.content.Context;
import android.media.AudioManager;

import static android.media.AudioManager.RINGER_MODE_NORMAL;
import static android.media.AudioManager.RINGER_MODE_VIBRATE;

/**
 * Created by ali77gh on 11/25/18.
 */

public class SilentManager {

    public static void Vibrate(Context context) {
        getAudioManager(context).setRingerMode(RINGER_MODE_VIBRATE);
    }

    public static void Normal(Context context) {
        getAudioManager(context).setRingerMode(RINGER_MODE_NORMAL);
    }

    private static AudioManager getAudioManager(Context context){
        return  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }
}
