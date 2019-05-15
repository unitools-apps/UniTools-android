package com.github.ali77gh.unitools.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.onlineapi.Promise;
import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;
import com.github.ali77gh.unitools.data.model.FilePack;
import com.github.ali77gh.unitools.ui.activities.FilePackActivity;
import com.github.ali77gh.unitools.ui.adapter.StoragePacksListViewAdapter;
import com.github.ali77gh.unitools.ui.dialogs.AddFilePackDialog;
import com.github.ali77gh.unitools.ui.dialogs.EditDocDialog;

import java.util.List;

/**
 * Created by ali on 10/3/18.
 */

public class DocsFragment extends Fragment implements Backable {

    private ConstraintLayout youHaveNoAccess;
    private ListView listView;


    public DocsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_docs, container, false);

        listView = view.findViewById(R.id.list_storage_main);
        FloatingActionButton fab = view.findViewById(R.id.fab_storage);
        youHaveNoAccess = view.findViewById(R.id.cons_storage_have_no_access);
        Button showPermissions = view.findViewById(R.id.btn_storage_grant_permission);

        showPermissions.setOnClickListener(view1 -> {
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO
            }, 0);

        });

        CheckPermission();

        listView.addFooterView(getActivity().getLayoutInflater().inflate(R.layout.layout_list_footer, null));

        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            if (!(view1.findViewById(R.id.text_storage_item_name) instanceof TextView))
                return; //on footer click
            Intent intent = new Intent(getActivity(), FilePackActivity.class);
            String folderName = ((TextView) view1.findViewById(R.id.text_storage_item_name)).getText().toString();
            intent.putExtra("path", FilePackProvider.getPathOfPack(folderName));
            intent.putExtra("docName", folderName);
            startActivity(intent);

        });

        listView.setOnItemLongClickListener((adapterView, view1, i, l) -> {
            if (i == FilePackProvider.getFilePacks().size()) return true;
            new EditDocDialog(getActivity(),FilePackProvider.getFilePacks().get(i).getName(),() -> RefreshList()).show();
            return true;
        });

        listView.setEmptyView(view.findViewById(R.id.text_storage_nothing_to_show));

        RefreshList();

        fab.setOnClickListener(view1 -> new AddFilePackDialog(getActivity(),
                new Promise<String>() {
            @Override
            public void onFailed(String msg) {
                //have no failed
            }

            @Override
            public void onSuccess(String output) {
                FilePackProvider.CreateFilePack(output);
                RefreshList();
            }
        }).show());
        return view;
    }

    private void RefreshList() {
        List<FilePack> filePacks = FilePackProvider.getFilePacks();
        if (filePacks != null)
            listView.setAdapter(new StoragePacksListViewAdapter(getActivity(), filePacks));
    }

    @Override
    public void onResume() {
        super.onResume();
        //when coming back from a file pack : so we should update file counts
        FilePackProvider.Init();
        RefreshList();
    }


    public void CheckPermission() {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            youHaveNoAccess.animate().alpha(0).setDuration(200).start();
            youHaveNoAccess.postDelayed(() -> youHaveNoAccess.setVisibility(View.GONE), 200);
            FilePackProvider.Init();
            return;
        }

        boolean storageR = getContext().checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean storageW = getContext().checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean camera = getContext().checkCallingOrSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean mic = getContext().checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;


        if (storageR && storageW && camera && mic) {
            youHaveNoAccess.animate().alpha(0).setDuration(200).start();
            youHaveNoAccess.postDelayed(() -> youHaveNoAccess.setVisibility(View.GONE), 200);
            FilePackProvider.Init();
        }
    }

    @Override
    public boolean onBack() {
        return false;
    }
}
