package com.github.ali77gh.unitools.uI.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.github.ali77gh.unitools.R;

/**
 * Created by ali on 10/3/18.
 */

public class AboutUsFragment extends Fragment {

    public AboutUsFragment() {
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
        View cView = inflater.inflate(R.layout.fragment_about_us, container, false);

        LinearLayout github = cView.findViewById(R.id.linear_about_github);
        LinearLayout playStore = cView.findViewById(R.id.linear_about_play_store);

        github.setOnClickListener(view -> {
            OpenGithub();
        });

        playStore.setOnClickListener(view -> {
            OpenPlayStore();
        });

        return cView;
    }

    private void OpenGithub() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/ali77gh/UniTools"));
        startActivity(browserIntent);
    }

    private void OpenPlayStore(){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://todoPlayStoreLinkHere"));
        startActivity(browserIntent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //todo destroy
    }
}
