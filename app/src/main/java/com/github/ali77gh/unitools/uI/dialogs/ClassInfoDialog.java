package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.repo.UClassRepo;

import static com.github.ali77gh.unitools.data.model.UClass.DISABLE_REMINDER;

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

        SetupAbsent();
        SetupReminder();

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

    private void SetupAbsent() {

        ImageView absentPlus = findViewById(R.id.image_home_class_info_dialog_plus);
        ImageView absentMinus = findViewById(R.id.image_home_class_info_dialog_minus);
        TextView absentCount = findViewById(R.id.text_home_class_info_dialog_absent_count);

        //load current
        absentCount.setText(String.valueOf(uClass.apcent));
        if (uClass.apcent >= 3)
            absentCount.setTextColor(CH.getColor(R.color.red));

        absentPlus.setOnClickListener(view -> {
            uClass.apcent++;
            absentCount.setText(String.valueOf(uClass.apcent));
            UClassRepo.Update(uClass);
            if (uClass.apcent >= 3)
                absentCount.setTextColor(CH.getColor(R.color.red));
        });

        absentMinus.setOnClickListener(view -> {
            if (uClass.apcent == 0) return;

            uClass.apcent--;
            absentCount.setText(String.valueOf(uClass.apcent));
            UClassRepo.Update(uClass);
            if (uClass.apcent < 3)
                absentCount.setTextColor(CH.getColor(R.color.black));
        });
    }

    private void SetupReminder() {
        CheckBox checkBox = findViewById(R.id.check_home_class_info_dialog_reminder);
        LinearLayout linearLayout = findViewById(R.id.linear_home_class_info_dialog_reminder);
        EditText editText = findViewById(R.id.edit_text_home_class_info_dialog_reminder);

        //load current
        if (uClass.reminder != DISABLE_REMINDER) {
            checkBox.setChecked(true);
            linearLayout.setVisibility(View.VISIBLE);
            editText.setText(String.valueOf(uClass.reminder / (60 * 1000)));
        }

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.GONE);
                uClass.reminder = DISABLE_REMINDER;
                UClassRepo.Update(uClass);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                uClass.reminder = Integer.valueOf(s.toString()) * 60 * 1000;
                UClassRepo.Update(uClass);
            }
        });
    }

    public void setEditListener(AddClassDialog.AddClassDialogListener editListener) {
        this.editListener = editListener;
    }
}
