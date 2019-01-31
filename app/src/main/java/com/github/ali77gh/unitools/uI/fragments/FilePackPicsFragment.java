package com.github.ali77gh.unitools.uI.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.uI.activities.FilePackActivity;
import com.github.ali77gh.unitools.uI.adapter.StoragePacksPicksListViewAdapter;
import com.github.ali77gh.unitools.uI.dialogs.FileActionDialog;

import java.io.File;
import java.net.URLConnection;

import static com.github.ali77gh.unitools.data.FileManager.FilePackProvider.IMAGE_PATH_NAME;

/**
 * Created by ali77gh on 12/13/18.
 */

public class FilePackPicsFragment extends Fragment {

    private StoragePacksPicksListViewAdapter adapter;

    private OnZoomableRequest onZoomableRequest;

    private ListView listView;
    private View nothingToShow;


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

        listView = view.findViewById(R.id.list_file_pack_pic);
        nothingToShow = view.findViewById(R.id.text_storage_nothing_to_show);

        RefreshList();

        return view;
    }

    private void RefreshList(){
        File[] images = new File(FilePackActivity.Path + File.separator + IMAGE_PATH_NAME).listFiles();
        FilePackProvider.Sort(images);

        adapter = new StoragePacksPicksListViewAdapter(getActivity(), images);
        listView.setAdapter(adapter);
        listView.setEmptyView(nothingToShow);

        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            onZoomableRequest.onRequest(images[i].getPath());
        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            new FileActionDialog(getActivity(), images[position], new FileActionDialog.FileActionDialogListener() {
                @Override
                public void onDelete() {
                    images[position].delete();
                    RefreshList();
                }

                @Override
                public void onShare() {
                    shareFile(images[position]);
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

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (adapter!=null){
            adapter.onLowMemory();
        }
    }

    public void setOnZoomableRequest(OnZoomableRequest onZoomableRequest) {
        this.onZoomableRequest = onZoomableRequest;
    }

    public interface OnZoomableRequest{
        void onRequest(String path);
    }
}
