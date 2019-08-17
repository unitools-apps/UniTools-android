package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.StringCoder;
import com.github.ali77gh.unitools.core.qrCode.QrCodeTools;
import com.github.ali77gh.unitools.data.model.Friend;
import com.github.ali77gh.unitools.data.repo.UClassRepo;
import com.google.gson.Gson;

;

/**
 * Created by ali77gh on 10/27/18.
 */

public class ShareClassesDialog extends BaseDialog {

    private Friend me;
    private BitmapProvider listener;

    public ShareClassesDialog(@NonNull Activity activity) {
        super(activity);
    }

    public ShareClassesDialog(@NonNull Activity activity, @NonNull Friend friend) {
        super(activity);
        this.me = friend;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_share_classes);

        ImageView qrCode = findViewById(R.id.image_share_classes_dialog_qr);
        Button cancel = findViewById(R.id.btn_share_classes_dialog_cancel);
        Button copy = findViewById(R.id.btn_share_classes_dialog_copy);

        if (me == null) {
            me = new Friend();
            me.classList = UClassRepo.getAll();
        }

        listener = bitmap -> copy.post(() -> qrCode.setImageBitmap(bitmap));

        thread.start();

        cancel.setOnClickListener(view -> dismiss());

        copy.setOnClickListener(view -> {
            ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setText(getActivity().getString(R.string.open_this_with_unitools_app) + "\n" +
                    "http://program.unitools/" + StringCoder.Encode(new Gson().toJson(me.getMinimal())));
            CH.toast(R.string.link_copied);
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
