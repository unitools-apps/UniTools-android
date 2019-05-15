package com.github.ali77gh.unitools.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.ui.activities.FilePackActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.github.ali77gh.unitools.data.FileManager.FilePackProvider.NOTES_PATH_NAME;

/**
 * Created by ali77gh on 12/13/18.
 */

public class FilePackNotesFragment extends Fragment {

    private ImageView background;
    private EditText input;
    private File textFile;

    public FilePackNotesFragment() {
        // Required empty public constructor
        String path = FilePackActivity.Path + File.separator + NOTES_PATH_NAME + File.separator + "1.txt";
        textFile = new File(path);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_filepack_notes, null);

        background = view.findViewById(R.id.image_file_pack_note_background);
        input = view.findViewById(R.id.txt_file_pack_note_input);

        input.setText(Load());

        return view;
    }

    public void Save() {

        //logically
        if (!textFile.exists()) {
            try {
                textFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("cant create new file" + e.getMessage());
            }
        }

        try {
            FileOutputStream stream = new FileOutputStream(textFile);
            stream.write(input.getText().toString().getBytes());
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException("cant write on file: " + e.getMessage());
        }

        //effect

        background.animate().setDuration(200).alpha(1).start();

        background.postDelayed(() -> {
            background.animate().setDuration(200).alpha((float) 0.5).start();
        },250);

    }

    @Override
    public void onPause() {
        try{
            Save();
        }catch (RuntimeException e){
            // this happened when user delete doc (fragment got killed)
        }
        super.onPause();
    }

    private String Load() {

        int length = (int) textFile.length();
        byte[] bytes = new byte[length];

        try {
            FileInputStream inputStream = new FileInputStream(textFile);
            inputStream.read(bytes);
            inputStream.close();
            return new String(bytes);
        } catch (IOException e) {
            return "";
        }

    }
}
