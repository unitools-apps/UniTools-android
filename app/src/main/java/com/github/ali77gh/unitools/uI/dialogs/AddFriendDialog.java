package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.qrCode.QrCodeTools;
import com.github.ali77gh.unitools.data.model.Friend;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

/**
 * Created by ali on 10/10/18.
 */

public class AddFriendDialog extends BaseDialog {

    private AddFriendDialogListener listener;
    private Friend mFriend;

    private ImageView statusIcon;
    private TextView statusText;

    public AddFriendDialog(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_friend);

        EditText name = findViewById(R.id.text_home_add_friend_dialog_name);
        Button qrCodeBtn = findViewById(R.id.btn_add_friend_dialog_qrcode);
        Button pasteBtn = findViewById(R.id.btn_add_friend_dialog_paste);
        LinearLayout status = findViewById(R.id.linear_add_friend_dialog_status);
        statusIcon = (ImageView) status.getChildAt(0);
        statusText = (TextView) status.getChildAt(1);
        Button cancel = findViewById(R.id.btn_home_add_friend_dialog_cancel);
        Button ok = findViewById(R.id.btn_home_add_friend_dialog_ok);

        ok.setOnClickListener(view -> {
            if (name.getText().toString().equals("")){
                Toast.makeText(getActivity(), getContext().getString(R.string.fill_blanks), Toast.LENGTH_SHORT).show();
                return;
            }

            if (mFriend == null){
                Toast.makeText(getActivity(),getContext().getString(R.string.first_set_friend),Toast.LENGTH_LONG).show();
                return;
            }

            mFriend.name = name.getText().toString();
            listener.onNewFriend(mFriend);
            dismiss();
        });

        pasteBtn.setOnClickListener(view -> {
            ClipboardManager clipboardManager =(ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            String friendString = clipboardManager.getText().toString() ;
            onFriendStringReady(friendString);
        });

        qrCodeBtn.setOnClickListener(view -> {
            QrCodeTools.LaunchCameraFromActivity(getActivity());
            // result will come in onFriendStringReady()
        });



        cancel.setOnClickListener(view -> {
            dismiss();
        });

    }

    public void onFriendStringReady(String friendString){
        try {
            mFriend = new Gson().fromJson(friendString, Friend.class);
            statusText.setText(getContext().getString(R.string.ready_to_add));
            statusIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.all_check));
            statusIcon.setColorFilter(getContext().getResources().getColor(R.color.green), PorterDuff.Mode.SRC_IN);
        }catch (JsonParseException e){
            statusText.setText(getContext().getString(R.string.not_set));
            statusIcon.setImageDrawable(getContext().getResources().getDrawable(R.drawable.all_close));
            statusIcon.setColorFilter(getContext().getResources().getColor(R.color.red), PorterDuff.Mode.SRC_IN);
        }
    }

    public void setListener(AddFriendDialogListener listener) {
        this.listener = listener;
    }

    public interface AddFriendDialogListener {
        void onNewFriend(Friend friend);
    }

}
