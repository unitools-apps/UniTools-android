package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.model.UserInfo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

/**
 * Created by ali77gh on 11/23/18.
 */

public class SettingsAlarmConfigDialog extends Dialog {

    private EditText hour;
    private EditText min;
    private Spinner notificationSpinner;
    private int selectedNotifiType;

    public SettingsAlarmConfigDialog(@NonNull Context context) {
        super(context);
        try {
            getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        }catch (NullPointerException ignored) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_settings_reminder_config);

        hour = findViewById(R.id.text_settings_reminder_dialog_hour);
        min = findViewById(R.id.text_settings_reminder_dialog_min);
        Button ok = findViewById(R.id.btn_settings_reminder_dialog_ok);
        Button cancel = findViewById(R.id.btn_settings_reminder_dialog_cancel);
        notificationSpinner = findViewById(R.id.spinner_settings_reminder_dialog_notifi_type);

        ok.setOnClickListener(view -> {

            if (!IsInt(hour.getText().toString()) | !IsInt(min.getText().toString())) {
                Toast.makeText(getContext(), getContext().getString(R.string.hour_or_min_is_not_valid), Toast.LENGTH_SHORT).show();
                return;
            }
            UserInfo ui = UserInfoRepo.getUserInfo();
            ui.ReminderInMins = (Integer.valueOf(hour.getText().toString()) * 60) + Integer.valueOf(min.getText().toString());
            ui.NotificationMode = selectedNotifiType;
            UserInfoRepo.setUserInfo(ui);
            dismiss();
        });

        cancel.setOnClickListener(view -> dismiss());

        LoadCurrent();
    }

    private void LoadCurrent() {

        String modes[] = getContext().getResources().getStringArray(R.array.notification_modes);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, modes);
        notificationSpinner.setAdapter(adapter);

        ((View) notificationSpinner.getParent()).setOnClickListener(view -> {
            notificationSpinner.performClick();
        });

        notificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        selectedNotifiType = UserInfo.NOTIFICATION_WITH_SOUND;
                        break;
                    case 1:
                        selectedNotifiType = UserInfo.NOTIFICATION_JUST_NOTIFI;
                        break;
                    case 2:
                        selectedNotifiType = UserInfo.NOTIFICATION_NOTHING;
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        UserInfo ui = UserInfoRepo.getUserInfo();
        hour.setText(String.valueOf ((int) ui.ReminderInMins /60));
        min.setText(String.valueOf ( ui.ReminderInMins %60));
        switch (ui.NotificationMode) {
            case UserInfo.NOTIFICATION_WITH_SOUND:
                notificationSpinner.setSelection(0);
                selectedNotifiType = UserInfo.NOTIFICATION_WITH_SOUND;
                break;
            case UserInfo.NOTIFICATION_JUST_NOTIFI:
                notificationSpinner.setSelection(1);
                selectedNotifiType = UserInfo.NOTIFICATION_JUST_NOTIFI;
                break;
            case UserInfo.NOTIFICATION_NOTHING:
                notificationSpinner.setSelection(2);
                selectedNotifiType = UserInfo.NOTIFICATION_NOTHING;
                break;
            default:
                throw new IllegalArgumentException("invalid Notification mode: " + ui.NotificationMode);
        }


    }

    private boolean IsInt(String s) {
        try {
            int a = Integer.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
