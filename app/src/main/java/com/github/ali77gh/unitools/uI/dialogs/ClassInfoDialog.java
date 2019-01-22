package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

public class ClassInfoDialog extends BaseDialog {

    private UClass uClass;
    private OnDeleteListener deleteListener;
    private AddClassDialog.AddClassDialogListener editListener;

    public ClassInfoDialog(@NonNull Activity activity, UClass uClass, OnDeleteListener deleteListener) {
        super(activity);
        this.uClass = uClass;
        this.deleteListener = deleteListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_class_info);

        ImageView edit = findViewById(R.id.image_home_class_info_dialog_edit);

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
            deleteListener.onDelete();
            dismiss();
        });

        cancel.setOnClickListener(view -> dismiss());

        edit.setOnClickListener(view -> {
            AddClassDialog addFriendDialog = new AddClassDialog(getActivity(), uClass);
            addFriendDialog.setListener(uClass2 -> {
                uClass2.id = uClass.id;
                editListener.onNewClass(uClass2);
                name.setText(Translator.getUClassReadable(uClass2));
            });
            addFriendDialog.show();
            Toast.makeText(getActivity(), getActivity().getString(R.string.enter_time_in_24_system), Toast.LENGTH_SHORT).show();
        });
    }

    public void setEditListener(AddClassDialog.AddClassDialogListener editListener) {
        this.editListener = editListener;
    }
}
