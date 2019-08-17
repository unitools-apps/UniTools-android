package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.core.qrCode.QrCodeTools;
import com.github.ali77gh.unitools.core.tools.Sort;
import com.github.ali77gh.unitools.data.model.Friend;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.repo.FriendRepo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by ali77gh on 11/10/18.
 */

public class FriendInfoDialog extends BaseDialog {

    private Friend friend;
    private OnDeleteListener deleteListener;
    private ListView listView;

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
        listView = findViewById(R.id.list_home_friend_info_dialog);
        ImageView addClass = findViewById(R.id.btn_home_friend_info_dialog_add_class);
        Button scanBarcode = findViewById(R.id.btn_home_friend_info_dialog_scan);
        Button cancel = findViewById(R.id.btn_home_friend_info_dialog_cancel);
        Button delete = findViewById(R.id.btn_home_friend_info_dialog_delete);

        //load info
        name.setText(friend.name);

        RefreshList();

        addClass.setOnClickListener(v -> {
            AddClassDialog addClassDialog = new AddClassDialog(getActivity(), null);
            addClassDialog.setListener(uClass -> {
                friend.classList.add(uClass);
                FriendRepo.Update(friend);
                RefreshList();
            });
            addClassDialog.show();
        });

        scanBarcode.setOnClickListener(v -> {
            QrCodeTools.LaunchCameraFromActivity(getActivity());
        });

        //setup events
        delete.setOnClickListener(view -> {
            deleteListener.onDelete();
            dismiss();
        });

        cancel.setOnClickListener(view -> dismiss());

        share.setOnClickListener(view -> new ShareClassesDialog(getActivity(), friend).show());
    }

    private void RefreshList() {
        //classes
        List<String> classesString = new ArrayList<>();
        List<UClass> uClasses = friend.classList;
        Sort.SortClass(uClasses);
        for (UClass uClass : uClasses) {
            classesString.add(Translator.getUClassReadable(uClass));
        }
        ArrayAdapter<String> classesStringAdapter = new ArrayAdapter<>(CH.getAppContext(), R.layout.item_spinner, classesString);
        listView.setAdapter(classesStringAdapter);
        listView.setEmptyView(findViewById(R.id.text_home_friend_info_dialog_nothing_to_show));

        listView.setOnItemClickListener((parent, view, position, id) -> {
            new ConfirmDeleteDialog(getActivity(), () -> {
                friend.classList.remove(position);
                FriendRepo.Update(friend);
                RefreshList();
            }).show();
        });
    }

    public void OnFriendStringReady(String friendStr) {
        try {
            Friend.MinimalFriend minimalFriend = new Gson().fromJson(friendStr, Friend.MinimalFriend.class);
            friend.classList = Friend.MinimalToFull(minimalFriend).classList;

            FriendRepo.Update(friend);

            RefreshList();
        } catch (RuntimeException e) {
            CH.toast(R.string.wrong_barcode);
        }
    }
}
