package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.data.Model.UClass;

/**
 * Created by ali77gh on 11/14/18.
 */

public class ClassInfoDialog extends Dialog {


    private UClass uClass;
    private OnDeleteListener listener;

    public ClassInfoDialog(@NonNull Context context, UClass uClass, OnDeleteListener listener) {
        super(context);
        this.uClass = uClass;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_class_info);
        TextView name = findViewById(R.id.txt_home_class_info_dialog_name);
        Button cancel = findViewById(R.id.btn_home_class_info_dialog_cancel);
        Button delete = findViewById(R.id.btn_home_class_info_dialog_delete);

        //load info
        name.setText(Translator.getUClassReadable(uClass));

        //setup events
        delete.setOnClickListener(view -> {
            listener.onDelete();
            dismiss();
        });

        cancel.setOnClickListener(view -> dismiss());

    }
}
