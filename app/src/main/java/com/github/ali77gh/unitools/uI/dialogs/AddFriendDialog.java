package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.model.Friend;

import java.util.ArrayList;

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
                Toast.makeText(getActivity(), getContext().getString(R.string.fill_blanks), Toast.LENGTH_SHORT).show();
                return;
            }
            Friend friend = new Friend();
            friend.name = name.getText().toString();
            friend.classList = new ArrayList<>();
            listener.onNewFriend(friend);
            dismiss();
        });

        cancel.setOnClickListener(view -> {
            dismiss();
        });

    }

    public void setListener(AddFriendDialogListener listener) {
        this.listener = listener;
    }

    public interface AddFriendDialogListener {
        void onNewFriend(Friend friend);
    }

}
