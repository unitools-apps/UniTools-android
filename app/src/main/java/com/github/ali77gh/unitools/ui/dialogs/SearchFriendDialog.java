package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.data.model.Friend;
import com.github.ali77gh.unitools.data.repo.FriendRepo;
import com.github.ali77gh.unitools.ui.adapter.WallFriendAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendDialog extends BaseDialog {

    private EditText searchInput;
    private TextView nothong_to_show;
    private ListView lst;

    public SearchFriendDialog(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search_friend);

        searchInput = findViewById(R.id.text_home_friend_search_dialog_input);
        nothong_to_show = findViewById(R.id.text_home_friend_search_dialog_nothing_to_show);
        lst = findViewById(R.id.list_home_friend_search_dialog);
        Button cancel = findViewById(R.id.btn_home_friend_search_dialog_cancel);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                RefreshList();
            }
        });
        RefreshList();

        cancel.setOnClickListener(v -> dismiss());
    }

    private void RefreshList() {
        //classes

        List<Friend> matchFriends = new ArrayList<>();
        for (Friend friend : FriendRepo.getAll()){
            if (friend.name.contains(searchInput.getText().toString())){
                matchFriends.add(friend);
            }
        }
        lst.setAdapter(new WallFriendAdapter(getActivity(), matchFriends));

        lst.setOnItemClickListener((adapterView, view, i, l) -> {
            new FriendInfoDialog(getActivity(), matchFriends.get(i), () -> {
                //on delete
                FriendRepo.Remove(matchFriends.get(i).id);
            }).show();
        });
        lst.setEmptyView(nothong_to_show);
    }
}
