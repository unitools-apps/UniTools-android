package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.qrCode.QrCodeTools;
import com.github.ali77gh.unitools.data.model.Friend;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;
import com.google.gson.Gson;

/**
 * Created by ali77gh on 10/27/18.
 */

public class ShareClassesDialog extends Dialog {

    private Friend me;
    private BitmapProvider listener;

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

        me = new Friend();
        me.classList = UserInfoRepo.getUserInfo().Classes;
        me.name = "";

        listener = bitmap -> copy.post(() -> qrCode.setImageBitmap(bitmap));

        thread.start();

        cancel.setOnClickListener(view -> dismiss());

        copy.setOnClickListener(view -> {
            ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(new Gson().toJson(me));//todo replace with setPrimaryClip()
            dismiss();
        });

    }

    private Thread thread = new Thread() {
        @Override
        public void run() {
            listener.onBitmapReady(QrCodeTools.Generate(me));

            super.run();
        }
    };

    private interface BitmapProvider {
        void onBitmapReady(Bitmap bitmap);
    }
}
