package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.FileManager.FilePackProvider;

public class EditDocDialog extends BaseDialog {

    private String name;
    private OnSomethingChanged onSomethingChanged;

    public EditDocDialog(@NonNull Activity activity, String name,OnSomethingChanged onSomethingChanged) {
        super(activity);
        this.name = name;
        this.onSomethingChanged = onSomethingChanged;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_doc);

        EditText newName = findViewById(R.id.text_edit_doc_dialog_rename);
        Button rename = findViewById(R.id.btn_edit_doc_dialog_rename);
        ImageView delete = findViewById(R.id.btn_edit_doc_dialog_delete);
        Button cancel = findViewById(R.id.btn_edit_doc_dialog_cancel);

        cancel.setOnClickListener(view -> dismiss());

        newName.setText(name);

        delete.setOnClickListener(view -> {
            new ConfirmDeleteDialog(getActivity(), () -> {
                FilePackProvider.DeleteFilePack(name);
                onSomethingChanged.onSomethingChanged();
                dismiss();
            }).show();
        });

        rename.setOnClickListener(view -> {
            FilePackProvider.RenameFilePack(name, newName.getText().toString());
            onSomethingChanged.onSomethingChanged();
            dismiss();
        });
    }

    public interface OnSomethingChanged{
        void onSomethingChanged();
    }

}
