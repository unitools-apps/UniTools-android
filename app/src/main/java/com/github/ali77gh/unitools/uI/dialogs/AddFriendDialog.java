package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.Model.Friend;

/**
 * Created by ali on 10/10/18.
 */

public class AddFriendDialog extends Dialog {

    private AddFriendDialogListener listener;
    private Context context;
    private boolean isReady = false;

    public AddFriendDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_friend);

        EditText name = findViewById(R.id.text_home_add_friend_dialog_name);
        Button qrCodeBtn = findViewById(R.id.btn_add_friend_dialog_qrcode);
        Button pasteBtn = findViewById(R.id.btn_add_friend_dialog_paste);
        LinearLayout status = findViewById(R.id.linear_add_friend_dialog_status);
        ImageView statusIcon = (ImageView) status.getChildAt(0);
        TextView statusText = (TextView) status.getChildAt(1);
        Button cancel = findViewById(R.id.btn_home_add_friend_dialog_cancel);
        Button ok = findViewById(R.id.btn_home_add_friend_dialog_ok);

        ok.setOnClickListener(view -> {
            if (name.getText().toString().equals("")){
                Toast.makeText(context, getContext().getString(R.string.fill_blanks), Toast.LENGTH_SHORT).show();
                return;
            }


        });

        pasteBtn.setOnClickListener(view -> {

        });

        qrCodeBtn.setOnClickListener(view -> {

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
