package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.core.tools.Sort;
import com.github.ali77gh.unitools.data.model.Friend;
import com.github.ali77gh.unitools.data.model.UClass;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ali77gh on 11/10/18.
 */

public class FriendInfoDialog extends BaseDialog {

    private Friend friend;
    private OnDeleteListener deleteListener;

    public FriendInfoDialog(@NonNull Activity activity, Friend friend, OnDeleteListener listener) {
        super(activity);
        this.friend = friend;
        this.deleteListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_friend_info);
        ImageView share = findViewById(R.id.image_home_friend_info_dialog_share);
        TextView name = findViewById(R.id.txt_home_friend_info_dialog_name);
        ListView listView = findViewById(R.id.list_home_friend_info_dialog);
        Button cancel = findViewById(R.id.btn_home_friend_info_dialog_cancel);
        Button delete = findViewById(R.id.btn_home_friend_info_dialog_delete);

        //load info
        name.setText(friend.name);

        //classes
        List<String> classesString = new ArrayList<>();
        List<UClass> uClasses = friend.classList;
        Sort.SortClass(uClasses);
        for (UClass uClass : uClasses) {
            classesString.add(Translator.getUClassReadable(uClass));
        }
        ArrayAdapter<String> classesStringAdapter = new ArrayAdapter<>(ContextHolder.getAppContext(), R.layout.item_spinner, classesString);
        listView.setAdapter(classesStringAdapter);


        //setup events
        delete.setOnClickListener(view -> {
            deleteListener.onDelete();
            dismiss();
        });

        cancel.setOnClickListener(view -> dismiss());

        share.setOnClickListener(view -> new ShareClassesDialog(getActivity(), friend).show());
    }
}
