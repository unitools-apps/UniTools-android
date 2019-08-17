package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.data.model.Friend;
import com.github.ali77gh.unitools.data.repo.FriendRepo;

import java.util.ArrayList;
import java.util.List;

;

/**
 * Created by ali on 10/10/18.
 */

public class AddFriendDialog extends BaseDialog {

    private AddFriendDialogListener listener;

    public AddFriendDialog(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_add_friend);

        EditText name = findViewById(R.id.text_home_add_friend_dialog_name);
        Button cancel = findViewById(R.id.btn_home_add_friend_dialog_cancel);
        Button ok = findViewById(R.id.btn_home_add_friend_dialog_ok);

        ok.setOnClickListener(view -> {
            if (name.getText().toString().equals("")){
                CH.toast(R.string.fill_blanks);
                return;
            }

            if (CheckFriendExist(name.getText().toString())){
                CH.toast(R.string.exists);
                return;
            }

            Friend friend = new Friend();
            friend.name = name.getText().toString();
            friend.classList = new ArrayList<>();
            listener.onNewFriend(friend);
            dismiss();
        });

        cancel.setOnClickListener(view -> dismiss());

    }

    public void setListener(AddFriendDialogListener listener) {
        this.listener = listener;
    }

    private boolean CheckFriendExist(String name){

        List<Friend> friends = FriendRepo.getAll();
        for(Friend friend : friends){
            if (friend.name.equals(name)) return true;
        }
        return false;
    }

    public interface AddFriendDialogListener {
        void onNewFriend(Friend friend);
    }

}
