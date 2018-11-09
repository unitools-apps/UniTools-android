package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.QrCode.QrCodeTools;
import com.github.ali77gh.unitools.data.Model.Friend;
import com.github.ali77gh.unitools.data.Repo.UserInfoRepo;
import com.google.gson.Gson;

/**
 * Created by ali77gh on 10/27/18.
 */

public class ShareClassesDialog extends Dialog {


    public ShareClassesDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_classes);

        ImageView qrCode = findViewById(R.id.image_share_classes_dialog_qr);
        Button cancel = findViewById(R.id.btn_share_classes_dialog_cancel);
        Button copy = findViewById(R.id.btn_share_classes_dialog_copy);

        Friend me = new Friend();
        me.classList = UserInfoRepo.getUserInfo().Classes;
        me.name = "";
        qrCode.setImageBitmap(QrCodeTools.Generate(me));

        cancel.setOnClickListener(view -> dismiss());

        copy.setOnClickListener(view -> {
            ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(new Gson().toJson(me));//todo replace with setPrimaryClip()
            dismiss();
        });

    }
}
