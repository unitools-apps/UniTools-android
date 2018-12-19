package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.ContextHolder;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_class_info);
        TextView name = findViewById(R.id.txt_home_class_info_dialog_name);
        Button cancel = findViewById(R.id.btn_home_class_info_dialog_cancel);
        Button delete = findViewById(R.id.btn_home_class_info_dialog_delete);

        ImageView absentPlus = findViewById(R.id.image_home_class_info_dialog_plus);
        ImageView absentMinus = findViewById(R.id.image_home_class_info_dialog_minus);
        TextView absentCount = findViewById(R.id.text_home_class_info_dialog_absent_count);

        absentCount.setText(String.valueOf(uClass.apcent));
        if (uClass.apcent >= 3)
            absentCount.setTextColor(ContextHolder.getAppContext().getResources().getColor(R.color.red));

        absentPlus.setOnClickListener(view -> {
            uClass.apcent++;
            absentCount.setText(String.valueOf(uClass.apcent));
            UserInfoRepo.UpdateClass(uClass);
            if (uClass.apcent >= 3)
                absentCount.setTextColor(ContextHolder.getAppContext().getResources().getColor(R.color.red));
        });

        absentMinus.setOnClickListener(view -> {
            if (uClass.apcent == 0) return;

            uClass.apcent--;
            absentCount.setText(String.valueOf(uClass.apcent));
            UserInfoRepo.UpdateClass(uClass);
            if (uClass.apcent < 3)
                absentCount.setTextColor(ContextHolder.getAppContext().getResources().getColor(R.color.white));
        });

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
