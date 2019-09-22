package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.MyDataBeen;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.data.model.UClass;
import com.github.ali77gh.unitools.data.repo.UClassRepo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

import java.util.ArrayList;
import java.util.List;

import static com.github.ali77gh.unitools.data.model.UClass.ReminderValues.DISABLE_REMINDER;
import static com.github.ali77gh.unitools.data.model.UClass.ReminderValues.REMINDER_15_MIN;
import static com.github.ali77gh.unitools.data.model.UClass.ReminderValues.REMINDER_1_HOUR;
import static com.github.ali77gh.unitools.data.model.UClass.ReminderValues.REMINDER_2_HOUR;
import static com.github.ali77gh.unitools.data.model.UClass.ReminderValues.REMINDER_30_MIN;
import static com.github.ali77gh.unitools.data.model.UClass.ReminderValues.REMINDER_3_HOUR;
import static com.github.ali77gh.unitools.data.model.UClass.ReminderValues.REMINDER_4_HOUR;

;

/**
 * Created by ali77gh on 11/14/18.
 */

public class ClassInfoDialog extends BaseDialog {

    private UClass uClass;
    private OnDeleteListener deleteListener;
    private AddClassDialog.AddClassDialogListener editListener;

    private TextView name;
    private TextView teacherName;

    private final static String REMINDER_15_MIN_NAME = "15 min";
    private final static String REMINDER_30_MIN_NAME = "30 min";
    private final static String REMINDER_1_HOUR_NAME = "1 hour";
    private final static String REMINDER_2_HOUR_NAME = "2 hour";
    private final static String REMINDER_3_HOUR_NAME = "3 hour";
    private final static String REMINDER_4_HOUR_NAME = "4 hour";


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

        name = findViewById(R.id.txt_home_class_info_dialog_name);
        teacherName = findViewById(R.id.txt_home_class_info_dialog_teacher);
        Button cancel = findViewById(R.id.btn_home_class_info_dialog_cancel);
        Button delete = findViewById(R.id.btn_home_class_info_dialog_delete);

        SetupAbsent();
        SetupReminder();

        //load info
        LoadData(uClass);

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
                LoadData(uClass2);
            });
            addFriendDialog.show();
            CH.toast(R.string.enter_time_in_24_system);
        });
    }

    private void LoadData(UClass uClass) {
        name.setText(Translator.getUClassReadable(uClass));
        if (uClass.teacherName == null){ // while user data is older then version 1.8.0
            uClass.teacherName = "";
            UClassRepo.Update(uClass);
        }
        if (!uClass.teacherName.equals("")) {
            teacherName.setText(CH.getString(R.string.teacher) + " : " + uClass.teacherName);
            teacherName.setVisibility(View.VISIBLE);
        } else {
            teacherName.setVisibility(View.GONE);
        }
    }

    private void SetupAbsent() {

        ImageView absentPlus = findViewById(R.id.image_home_class_info_dialog_plus);
        ImageView absentMinus = findViewById(R.id.image_home_class_info_dialog_minus);
        TextView absentCount = findViewById(R.id.text_home_class_info_dialog_absent_count);

        //load current
        absentCount.setText(String.valueOf(uClass.absence));
        if (uClass.absence >= 3)
            absentCount.setTextColor(CH.getColor(R.color.red));

        absentPlus.setOnClickListener(view -> {
            uClass.absence++;
            absentCount.setText(String.valueOf(uClass.absence));
            UClassRepo.Update(uClass);
            if (uClass.absence >= 3)
                absentCount.setTextColor(CH.getColor(R.color.red));
        });

        absentMinus.setOnClickListener(view -> {
            if (uClass.absence == 0) return;

            uClass.absence--;
            absentCount.setText(String.valueOf(uClass.absence));
            UClassRepo.Update(uClass);
            if (uClass.absence < 3)
                absentCount.setTextColor(CH.getColor(R.color.black));
        });
    }

    private void SetupReminder() {
        CheckBox checkBox = findViewById(R.id.check_home_class_info_dialog_reminder);
        LinearLayout linearLayout = findViewById(R.id.linear_home_class_info_dialog_reminder);
        Spinner spinner = findViewById(R.id.spinner_text_home_class_info_dialog_reminder);

        SetupSpinners(spinner);

        // load current
        if (uClass.reminder != DISABLE_REMINDER) {
            checkBox.setChecked(true);
            linearLayout.setVisibility(View.VISIBLE);
            switch (uClass.reminder){
                case REMINDER_15_MIN:
                    spinner.setSelection(0);
                    break;
                case REMINDER_30_MIN:
                    spinner.setSelection(1);
                    break;
                case REMINDER_1_HOUR:
                    spinner.setSelection(2);
                    break;
                case REMINDER_2_HOUR:
                    spinner.setSelection(3);
                    break;
                case REMINDER_3_HOUR:
                    spinner.setSelection(4);
                    break;
                case REMINDER_4_HOUR:
                    spinner.setSelection(5);
                    break;
                default:
                    throw new RuntimeException("invalid reminder value");
            }
        }

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                linearLayout.setVisibility(View.VISIBLE);
                spinner.setSelection(0);
                uClass.reminder = REMINDER_15_MIN;
                UClassRepo.Update(uClass);
            } else {
                linearLayout.setVisibility(View.GONE);
                uClass.reminder = DISABLE_REMINDER;
                UClassRepo.Update(uClass);
            }
            MyDataBeen.onNewAlarm();
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (((TextView) view).getText().toString()){
                    case REMINDER_15_MIN_NAME:
                        uClass.reminder = REMINDER_15_MIN;
                        break;
                    case REMINDER_30_MIN_NAME:
                        uClass.reminder = REMINDER_30_MIN;
                        break;
                    case REMINDER_1_HOUR_NAME:
                        uClass.reminder = REMINDER_15_MIN;
                        break;
                    case REMINDER_2_HOUR_NAME:
                        uClass.reminder = REMINDER_2_HOUR;
                        break;
                    case REMINDER_3_HOUR_NAME:
                        uClass.reminder = REMINDER_3_HOUR;
                        break;
                    case REMINDER_4_HOUR_NAME:
                        uClass.reminder = REMINDER_4_HOUR;
                        break;
                    default:
                        throw new RuntimeException("invalid reminder value");
                }
                UClassRepo.Update(uClass);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setEditListener(AddClassDialog.AddClassDialogListener editListener) {
        this.editListener = editListener;
    }

    private void SetupSpinners(Spinner spinner) {

        List<String> times = new ArrayList<>();

        times.add(REMINDER_15_MIN_NAME);
        times.add(REMINDER_30_MIN_NAME);
        times.add(REMINDER_1_HOUR_NAME);
        times.add(REMINDER_2_HOUR_NAME);
        times.add(REMINDER_3_HOUR_NAME);
        times.add(REMINDER_4_HOUR_NAME);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, times);
        spinner.setAdapter(adapter);
    }
}
