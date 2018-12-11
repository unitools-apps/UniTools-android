package com.github.ali77gh.unitools.uI.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.onlineapi.Promise;
import com.github.ali77gh.unitools.data.model.FilePack;
import com.github.ali77gh.unitools.uI.adapter.StoragePacksListViewAdapter;
import com.github.ali77gh.unitools.uI.dialogs.AddFilePackDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali on 10/3/18.
 */

public class StorageFragment extends Fragment implements Backable {

    private ConstraintLayout youHaveNoAccess;


    public StorageFragment() {
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
        View view = inflater.inflate(R.layout.fragment_storage, container, false);

        ListView listView = view.findViewById(R.id.list_storage_main);
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

        //test
        List<FilePack> lst = new ArrayList<>();
        lst.add(new FilePack("ممد", 20, 6));
        lst.add(new FilePack("جعفری", 32, 2));
        lst.add(new FilePack("پایگاه داده", 18, 16));
        lst.add(new FilePack("فیلان", 103, 3));
        lst.add(new FilePack("بیسار", 2, 1));

        listView.setAdapter(new StoragePacksListViewAdapter(getActivity(), lst));

        fab.setOnClickListener(view1 -> {
            new AddFilePackDialog(getActivity(), new Promise<List<String>>() {
                @Override
                public void onFailed(String msg) {

                }

                @Override
                public void onSuccess(List<String> output) {

                }
            }).show();
        });
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //todo destroy
    }

    public void CheckPermission() {

        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            youHaveNoAccess.animate().alpha(0).setDuration(200).start();
            youHaveNoAccess.postDelayed(() -> youHaveNoAccess.setVisibility(View.GONE), 200);
            return;
        }

        boolean storageR = getContext().checkCallingOrSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean storageW = getContext().checkCallingOrSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean camera = getContext().checkCallingOrSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean mic = getContext().checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;


        if (storageR && storageW && camera && mic) {
            youHaveNoAccess.animate().alpha(0).setDuration(200).start();
            youHaveNoAccess.postDelayed(() -> youHaveNoAccess.setVisibility(View.GONE), 200);
        }
    }

    @Override
    public boolean onBack() {
        return false;
    }
}
