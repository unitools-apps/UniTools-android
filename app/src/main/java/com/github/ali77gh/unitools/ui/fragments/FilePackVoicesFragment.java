package com.github.ali77gh.unitools.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.audio.VoicePlayer;
import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.ui.activities.FilePackActivity;
import com.github.ali77gh.unitools.ui.adapter.StoragePacksVoicesListViewAdapter;
import com.github.ali77gh.unitools.ui.dialogs.FileActionDialog;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLConnection;

import static com.github.ali77gh.unitools.data.FileManager.FilePackProvider.VOICE_PATH_NAME;

/**
 * Created by ali77gh on 12/13/18.
 */

public class FilePackVoicesFragment extends Fragment {

    private ListView listView;
    private View nothingToShow;
    private VoicePlayer voicePlayer;

    public FilePackVoicesFragment() {
        voicePlayer = new VoicePlayer();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filepack_voices, null);

        listView = view.findViewById(R.id.list_file_pack_voice);
        nothingToShow = view.findViewById(R.id.text_storage_nothing_to_show);

        RefreshList();

        return view;
    }

    public void RefreshList() {
        File[] voices = new File(FilePackActivity.Path + File.separator + VOICE_PATH_NAME).listFiles();

        FilePackProvider.Sort(voices);

        StoragePacksVoicesListViewAdapter adapter = new StoragePacksVoicesListViewAdapter(getActivity(), voices);
        listView.setAdapter(adapter);
        listView.setEmptyView(nothingToShow);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {

            if (Build.VERSION.SDK_INT >= 24) {
                try {
                    Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                    m.invoke(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("file:///" + voices[i].getPath());
            intent.setDataAndType(data, "audio/mp3");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {

            new FileActionDialog(getActivity(), voices[position], new FileActionDialog.FileActionDialogListener() {
                @Override
                public void onDelete() {
                    voices[position].delete();
                    RefreshList();
                }

                @Override
                public void onShare() {
                    shareFile(voices[position]);
                    RefreshList();
                }
            }).show();
            return true;
        });
    }

    private void shareFile(File file) {

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
        intentShareFile.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file://"+file.getAbsolutePath()));

        startActivity(Intent.createChooser(intentShareFile, "Share File"));

    }
}
