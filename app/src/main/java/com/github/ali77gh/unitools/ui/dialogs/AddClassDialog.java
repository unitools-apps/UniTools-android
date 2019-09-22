package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.data.model.Time;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.repo.UClassRepo;

/**
 * Created by ali77gh on 10/12/18.
 */

public class AddClassDialog extends BaseDialog {

    private AddClassDialogListener listener;
    private Spinner daySpinner;
    private UClass uClass;

    public AddClassDialog(@NonNull Activity activity, @Nullable UClass uClass) {
        super(activity);
        this.uClass = uClass;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_add_class);

        TextView title = findViewById(R.id.text_home_add_class_dialog_title);
        daySpinner = findViewById(R.id.spinner_home_add_class_day);
        EditText label = findViewById(R.id.text_home_add_class_dialog_lable);
        EditText where = findViewById(R.id.text_home_add_class_dialog_where);
        EditText hour = findViewById(R.id.text_home_add_class_dialog_hour);
        EditText min = findViewById(R.id.text_home_add_class_dialog_min);
        EditText teacher = findViewById(R.id.text_home_add_class_dialog_teacher);

        // fill fields if edit mode
        if (uClass != null) {
            daySpinner.post(() -> daySpinner.setSelection(uClass.time.dayOfWeek));
            title.setText(getActivity().getResources().getString(R.string.edit_new_class));
            label.setText(uClass.what);
            where.setText(uClass.where);

            if (uClass.teacherName == null) { // while user data is older then version 1.8.0
                uClass.teacherName = "";
                UClassRepo.Update(uClass);
            }
            teacher.setText(uClass.teacherName);

            hour.setText(String.valueOf(uClass.time.hour));
            min.setText(String.valueOf(uClass.time.min));
        }

        hour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int count) {
                if (hour.getText().toString().length() == 2)
                    min.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        SetupSpinners();

        Button cancel = findViewById(R.id.btn_home_add_class_dialog_cancel);
        Button ok = findViewById(R.id.btn_home_add_class_dialog_ok);

        ok.setOnClickListener(view -> {

            if (!IsInt(hour.getText().toString()) | !IsInt(min.getText().toString())) {
                CH.toast(R.string.hour_or_min_is_not_valid);
                return;
            }

            if (!Time.Validator(Integer.valueOf(hour.getText().toString()), Integer.valueOf(min.getText().toString()))) {
                CH.toast(R.string.hour_or_min_is_not_valid);
                return;
            }

            if (label.getText().toString().equals("") | where.getText().toString().equals("")) {
                CH.toast(R.string.fill_blanks);
                return;
            }

            if (uClass == null) uClass = new UClass();

            uClass.where = where.getText().toString();
            uClass.what = label.getText().toString();
            uClass.teacherName = teacher.getText().toString();
            uClass.time = new Time(daySpinner.getSelectedItemPosition(), Integer.valueOf(hour.getText().toString()), Integer.valueOf(min.getText().toString()));

            listener.onNewClass(uClass);
            dismiss();
        });

        cancel.setOnClickListener(view -> dismiss());
    }

    public void setListener(AddClassDialogListener listener) {
        this.listener = listener;
    }

    private void SetupSpinners() {
        String modes[] = getContext().getResources().getStringArray(R.array.weekDays);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, modes);
        daySpinner.setAdapter(adapter);
    }

    public interface AddClassDialogListener {
        void onNewClass(UClass uClass);
    }

    private boolean IsInt(String s) {
        try {
            Integer.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
