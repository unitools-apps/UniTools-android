package com.github.ali77gh.unitools.uI.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.ali77gh.unitools.R;

/**
 * Created by ali on 10/3/18.
 */

public class TransferFragment extends Fragment implements Backable {

    public TransferFragment() {
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
        return inflater.inflate(R.layout.fragment_transfer, container, false);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //todo destroy
    }

    @Override
    public boolean onBack() {
        return false;
    }
}
