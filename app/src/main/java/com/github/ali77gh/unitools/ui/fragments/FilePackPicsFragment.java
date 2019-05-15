package com.github.ali77gh.unitools.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.ui.activities.FilePackActivity;
import com.github.ali77gh.unitools.ui.adapter.StoragePacksPicksViewAdapter;
import com.github.ali77gh.unitools.ui.dialogs.FileActionDialog;

import java.io.File;
import java.net.URLConnection;

import static com.github.ali77gh.unitools.data.FileManager.FilePackProvider.IMAGE_PATH_NAME;

/**
 * Created by ali77gh on 12/13/18.
 */

public class FilePackPicsFragment extends Fragment {

    private StoragePacksPicksViewAdapter adapter;

    private OnZoomableRequest onZoomableRequest;

    private boolean isListView = true;

    private ListView listView;
    private GridView gridView;
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
        gridView = view.findViewById(R.id.grid_file_pack_pic);
        nothingToShow = view.findViewById(R.id.text_storage_nothing_to_show);
        FloatingActionButton switchViewModeFab = view.findViewById(R.id.fab_switch_list_grid_mode);

        switchViewModeFab.setOnClickListener(v -> {

            if (listView.getVisibility() == View.VISIBLE) {
                isListView = false;
                listView.setVisibility(View.GONE);
                gridView.setVisibility(View.VISIBLE);
                switchViewModeFab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.docs_view_option_list));
            } else {
                isListView = true;
                listView.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.GONE);
                switchViewModeFab.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.docs_view_option_grid));
            }
            RefreshList();
        });

        RefreshList();

        return view;
    }

    public void RefreshList() {
        File[] images = new File(FilePackActivity.Path + File.separator + IMAGE_PATH_NAME).listFiles();
        FilePackProvider.Sort(images);

        AdapterView.OnItemClickListener onItemClickListener = (parent, view, position, id) -> onZoomableRequest.onRequest(images[position].getPath());

        AdapterView.OnItemLongClickListener onItemLongClickListener = (parent, view, position, id) -> {
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
        };
        if (isListView) {

            //clear grid
            gridView.setAdapter(null);
            gridView.setVisibility(View.GONE);

            //setup list
            listView.setVisibility(View.VISIBLE);

            adapter = new StoragePacksPicksViewAdapter(getActivity(), images, StoragePacksPicksViewAdapter.List);
            listView.setAdapter(adapter);
            listView.setEmptyView(nothingToShow);
            listView.setOnItemClickListener(onItemClickListener);
            listView.setOnItemLongClickListener(onItemLongClickListener);
        } else {

            //clear list
            listView.setAdapter(null);
            listView.setVisibility(View.GONE);

            //setup grid
            gridView.setVisibility(View.VISIBLE);

            adapter = new StoragePacksPicksViewAdapter(getActivity(), images, StoragePacksPicksViewAdapter.Grid);
            gridView.setAdapter(adapter);
            gridView.setEmptyView(nothingToShow);
            gridView.setOnItemClickListener(onItemClickListener);
            gridView.setOnItemLongClickListener(onItemLongClickListener);

        }
    }

    private void shareFile(File file) {

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);

        intentShareFile.setType(URLConnection.guessContentTypeFromName(file.getName()));
        intentShareFile.putExtra(Intent.EXTRA_STREAM,
                Uri.parse("file://" + file.getAbsolutePath()));

        startActivity(Intent.createChooser(intentShareFile, "Share File"));
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (adapter != null) {
            adapter.onLowMemory();
        }
    }

    public void setOnZoomableRequest(OnZoomableRequest onZoomableRequest) {
        this.onZoomableRequest = onZoomableRequest;
    }

    public interface OnZoomableRequest {
        void onRequest(String path);
    }
}
