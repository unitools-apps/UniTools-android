package com.github.ali77gh.unitools.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.model.FilePack;

import java.util.List;

public class StoragePacksListViewAdapter extends BaseAdapter {

    private Activity _activity;
    private List<FilePack> filePacks;

    public StoragePacksListViewAdapter(Activity activity, List<FilePack> filePacks) {

        this._activity = activity;
        this.filePacks = filePacks;
    }

    @Override
    public int getCount() {
        return filePacks.size();
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

        View cview = _activity.getLayoutInflater().inflate(R.layout.item_storage_pack, null);
        FilePack filePack = filePacks.get(i);

        TextView packName = cview.findViewById(R.id.text_storage_item_name);
        TextView picCount = cview.findViewById(R.id.text_storage_item_pic_count);
        TextView voiceCount = cview.findViewById(R.id.text_storage_item_voice_count);
        TextView pdfCount = cview.findViewById(R.id.text_storage_item_pdf_count);

        packName.setText(filePack.getName());
        picCount.setText(String.valueOf(filePack.getPicCount()));
        voiceCount.setText(String.valueOf(filePack.getVoiceCount()));
        pdfCount.setText(String.valueOf(filePack.getPdfCount()));

        return cview;
    }
}