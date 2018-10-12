package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.Model.Friend;

/**
 * Created by ali on 10/10/18.
 */

public class AddFriendDialog extends Dialog {

    private AddFriendDialogListener listener;

    public AddFriendDialog(@NonNull Context context) {
        super(context);

        setContentView(R.layout.dialog_add_friend);
    }

    public void setListener(AddFriendDialogListener listener) {
        this.listener = listener;
    }

    public interface AddFriendDialogListener {
        void onNewFriend(Friend friend);

        void onCancel();
    }

}
