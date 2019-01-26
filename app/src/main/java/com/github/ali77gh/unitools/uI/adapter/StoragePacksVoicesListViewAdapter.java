package com.github.ali77gh.unitools.uI.adapter;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.uI.Tools;

import java.io.File;

public class StoragePacksVoicesListViewAdapter extends BaseAdapter {

    private Activity _activity;
    private File[] files;
    public int nowPlayingPos = -1;

    public StoragePacksVoicesListViewAdapter(Activity activity, File[] filePacks) {

        this._activity = activity;
        this.files = filePacks;
    }

    @Override
    public int getCount() {
        return files.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewGroup cview = (ViewGroup) _activity.getLayoutInflater().inflate(R.layout.item_file_pack_voices, null);
        File voice = files[i];

        ImageView preview = (ImageView) cview.getChildAt(0);
        TextView name = (TextView) cview.getChildAt(1);
        TextView time = (TextView) cview.getChildAt(2);

        name.setText(String.valueOf(i));
        time.setText(getAudioFileTime(voice));

        if (i == nowPlayingPos) {
            preview.setImageDrawable(_activity.getDrawable(R.drawable.storage_voices_pause));
        } else {
            preview.setImageDrawable(_activity.getDrawable(R.drawable.storage_voices_play));
        }

        if (i == files.length - 1) {
            cview.setPadding(0, 0, 0, Tools.DpToPixel(100));
        }

        return cview;
    }

    private String getAudioFileTime(File file) {
        MediaPlayer mp = MediaPlayer.create(_activity, Uri.parse(file.getPath()));
        if (mp == null) return "err";
        int duration = mp.getDuration();
        duration /= 1000;
        int min = duration / 60;
        int sec = duration % 60;

        String minString = min > 9 ? String.valueOf(min) : "0" + String.valueOf(min);
        String secString = sec > 9 ? String.valueOf(sec) : "0" + String.valueOf(sec);

        return minString + ":" + secString;
    }
}