package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;

import com.github.ali77gh.unitools.R;

public class ConfirmDeleteDialog extends BaseDialog {

    private OnDeleteListener listener;

    public ConfirmDeleteDialog(@NonNull Activity activity , OnDeleteListener listener) {
        super(activity);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_delete);

        Button delete = findViewById(R.id.btn_settings_donate_dialog_delete);
        Button cancel = findViewById(R.id.btn_settings_donate_dialog_cancel);

        cancel.setOnClickListener(view -> {
            dismiss();
        });

        delete.setOnClickListener(view -> {
            listener.onDelete();
            dismiss();
        });
    }
}
