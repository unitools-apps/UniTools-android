package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;

import java.io.File;

public class FileActionDialog extends BaseDialog {

    private FileActionDialogListener listener;
    private File file;

    public FileActionDialog(@NonNull Activity activity, File path, @NonNull FileActionDialogListener listener) {
        super(activity);
        this.listener = listener;
        this.file = path;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_file_action);

        TextView name = findViewById(R.id.txt_file_action_dialog_name);
        ImageView share = findViewById(R.id.btn_file_action_dialog_share);
        ImageView delete = findViewById(R.id.btn_file_action_dialog_delete);
        Button back = findViewById(R.id.btn_file_action_dialog_back);

        back.setOnClickListener(v -> dismiss());

        name.setText(file.getName());

        share.setOnClickListener(v -> {
            listener.onShare();
            dismiss();
        });

        delete.setOnClickListener(v -> {
            listener.onDelete();
            dismiss();
        });

    }

    public interface FileActionDialogListener {
        void onDelete();

        void onShare();
    }
}
