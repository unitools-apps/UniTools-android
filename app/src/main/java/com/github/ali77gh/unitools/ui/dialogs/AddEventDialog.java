package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ali.uneversaldatetools.date.GregorianDateTime;
import com.ali.uneversaldatetools.date.JalaliDateTime;
import com.ali.uneversaldatetools.date.TimeZoneHelper;
import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.data.model.Event;
import com.github.ali77gh.unitools.data.model.Time;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

;

/**
 * Created by ali77gh on 10/17/18.
 */

public class AddEventDialog extends BaseDialog {

    private EventDialogListener listener;
    private Event event;
    private long selectedTime = -1;

    public AddEventDialog(@NonNull Activity activity, @Nullable Event event) {
        super(activity);
        this.event = event;
        if (event != null)
            selectedTime = event.unixTime;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_add_event);

        TextView title = findViewById(R.id.text_home_add_event_title);
        TextView openDatePicker = findViewById(R.id.text_home_add_event_dialog_date_picker);
        EditText what = findViewById(R.id.text_home_add_event_dialog_lable);
        EditText hour = findViewById(R.id.text_home_add_event_dialog_hour);
        EditText min = findViewById(R.id.text_home_add_event_dialog_min);
        Button cancel = findViewById(R.id.btn_home_add_event_dialog_cancel);
        Button ok = findViewById(R.id.btn_home_add_event_dialog_ok);

        openDatePicker.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog();
            datePickerDialog.setListener((unixTime, date) -> {

                if (DateTimeTools.UnixTimeToWeek((int) unixTime) < 0) {
                    CH.toast(R.string.date_is_not_valid);
                    return;
                }
                openDatePicker.setText(Translator.getDateString(date));

                selectedTime = unixTime;
            });
            datePickerDialog.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "");
        });

        // fill fields if edit mode
        if (event != null) {
            if (UserInfoRepo.getUserInfo().Calendar == 'j') {
                openDatePicker.setText(Translator.getDateString(new JalaliDateTime((int) event.unixTime, TimeZoneHelper.getSystemTimeZone())));
            } else if (UserInfoRepo.getUserInfo().Calendar == 'g') {
                openDatePicker.setText(Translator.getDateString(new GregorianDateTime((int) event.unixTime, TimeZoneHelper.getSystemTimeZone())));
            } else {
                throw new RuntimeException("invalid date system id:" + UserInfoRepo.getUserInfo().Calendar);
            }

            title.setText(getActivity().getResources().getString(R.string.edit_new_event));
            what.setText(event.what);
            hour.setText(String.valueOf(new JalaliDateTime((int) event.unixTime, TimeZoneHelper.getSystemTimeZone()).getHour()));
            min.setText(String.valueOf(new JalaliDateTime((int) event.unixTime, TimeZoneHelper.getSystemTimeZone()).getMin()));
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

        ok.setOnClickListener(view -> {

            if (!IsInt(hour.getText().toString()) |
                    !IsInt(min.getText().toString())) {
                CH.toast(R.string.hour_or_min_is_not_valid);
                return;
            }

            if (!Time.Validator(Integer.valueOf(hour.getText().toString()), Integer.valueOf(min.getText().toString()))) {
                CH.toast(R.string.hour_or_min_is_not_valid);
                return;
            }

            if (what.getText().toString().equals("")) {
                CH.toast(R.string.fill_blanks);
                return;
            }

            if (selectedTime == -1) {
                CH.toast(R.string.first_select_date);
                return;
            }

            if (event == null) event = new Event();
            event.what = what.getText().toString();
            GregorianDateTime dateTime = new GregorianDateTime((int) selectedTime, TimeZoneHelper.getSystemTimeZone());
            event.unixTime = new GregorianDateTime(
                    dateTime.getYear(),
                    dateTime.getMonth(),
                    dateTime.getDay(),
                    Integer.valueOf(hour.getText().toString()),
                    Integer.valueOf(min.getText().toString()),
                    0,
                    TimeZoneHelper.getSystemTimeZone()
            ).toUnixTime();


            listener.onNewEvent(event);
            dismiss();
        });

        cancel.setOnClickListener(view -> dismiss());
    }

    public void setListener(EventDialogListener listener) {
        this.listener = listener;
    }

    public interface EventDialogListener {
        void onNewEvent(Event event);
    }

    private boolean IsInt(String s) {
        try {
            Integer.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String getWeekString(int index) {
        return getActivity().getResources().getStringArray(R.array.week_days)[index];
    }
}
