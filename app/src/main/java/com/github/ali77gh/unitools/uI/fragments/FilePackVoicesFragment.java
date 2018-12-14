package com.github.ali77gh.unitools.uI.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.audio.VoicePlayer;
import com.github.ali77gh.unitools.uI.activities.FilePackActivity;
import com.github.ali77gh.unitools.uI.adapter.StoragePacksPicksListViewAdapter;
import com.github.ali77gh.unitools.uI.adapter.StoragePacksVoicesListViewAdapter;

import java.io.File;

import static com.github.ali77gh.unitools.data.FileManager.FilePackProvider.IMAGE_PATH_NAME;
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

        Sort(voices);

        StoragePacksVoicesListViewAdapter adapter = new StoragePacksVoicesListViewAdapter(getActivity(), voices);
        listView.setAdapter(adapter);
        listView.setEmptyView(nothingToShow);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (voicePlayer.IsPlaying()){
                voicePlayer.Stop();
                if (adapter.nowPlayingPos != i) {
                    voicePlayer.Play(voices[i].getPath());
                    adapter.nowPlayingPos =i;
                }
            }else {
                voicePlayer.Play(voices[i].getPath());
                adapter.nowPlayingPos =i;
            }
        });
    }


    private void Sort(File[] files) {
        File temp;
        for (int i = 0; i < files.length - 1; i++) {
            for (int j = i + 1; j < files.length; j++) {
                if (files[i].lastModified() > files[j].lastModified()) {
                    temp = files[i];
                    files[i] = files[j];
                    files[j] = temp;
                }
            }
        }
    }
}
