package com.github.ali77gh.unitools.uI.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.uI.activities.FilePackActivity;
import com.github.ali77gh.unitools.uI.adapter.StoragePacksPicksListViewAdapter;

import java.io.File;

import static com.github.ali77gh.unitools.data.FileManager.FilePackProvider.IMAGE_PATH_NAME;

/**
 * Created by ali77gh on 12/13/18.
 */

public class FilePackPicsFragment extends Fragment {

    private StoragePacksPicksListViewAdapter adapter;

    private OnZoomableRequest onZoomableRequest;

    public FilePackPicsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_filepack_pics, null);

        ListView listView = view.findViewById(R.id.list_file_pack_pic);
        View nothingToShow = view.findViewById(R.id.text_storage_nothing_to_show);

        File[] images = new File(FilePackActivity.Path + File.separator + IMAGE_PATH_NAME).listFiles();
        Sort(images);

        adapter = new StoragePacksPicksListViewAdapter(getActivity(), images);
        listView.setAdapter(adapter);
        listView.setEmptyView(nothingToShow);

        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            onZoomableRequest.onRequest(images[i].getPath());
        });

        return view;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (adapter!=null){
            adapter.onLowMemory();
        }
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

    public void setOnZoomableRequest(OnZoomableRequest onZoomableRequest) {
        this.onZoomableRequest = onZoomableRequest;
    }

    public interface OnZoomableRequest{
        void onRequest(String path);
    }
}
