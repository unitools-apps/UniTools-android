package com.github.ali77gh.unitools.uI.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.model.StoragePack;

import java.util.List;

public class StoragePacksListViewAdapter extends BaseAdapter {

    private Activity _activity;
    private List<StoragePack> storagePacks;

    public StoragePacksListViewAdapter(Activity activity, List<StoragePack> storagePacks) {

        this._activity = activity;
        this.storagePacks = storagePacks;
    }

    @Override
    public int getCount() {
        return storagePacks.size();
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
        View cview;
        if (view != null) {
            cview = view;
        } else {
            cview = _activity.getLayoutInflater().inflate(R.layout.item_storage_pack, null);
        }

        StoragePack storagePack = storagePacks.get(i);

        TextView packName = cview.findViewById(R.id.text_storage_item_name);
        TextView picCount = cview.findViewById(R.id.text_storage_item_pic_count);
        TextView voiceCount = cview.findViewById(R.id.text_storage_item_voice_count);

        packName.setText(storagePack.getName());
        picCount.setText(String.valueOf(storagePack.getPicCount()) );
        voiceCount.setText(String.valueOf(storagePack.getVoiceCount()));

        return cview;
    }


}