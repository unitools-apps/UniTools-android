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
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.tools.DateTimeTools;
import com.github.ali77gh.unitools.data.model.Event;
import com.github.ali77gh.unitools.data.model.Time;

/**
 * Created by ali77gh on 10/17/18.
 */

public class AddEventDialog extends BaseDialog {

    private EventDialogListener listener;
    private Event event;
    private int weekNumber = 10;
    private int dayOfWeek = 10;

    public AddEventDialog(@NonNull Activity activity, @Nullable Event event) {
        super(activity);
        this.event = event;
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
                weekNumber = DateTimeTools.UnixTimeToWeek((int) unixTime);
                if (weekNumber < 0) {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.date_is_not_valid), Toast.LENGTH_SHORT).show();
                    return;
                }
                openDatePicker.setText(getActivity().getString(R.string.week) +
                        " " +
                        String.valueOf(weekNumber) +
                        " " +
                        getWeekString(date.getDayOfWeek().getValue())
                );
                dayOfWeek = date.getDayOfWeek().getValue();
            });
            datePickerDialog.show(((AppCompatActivity) getActivity()).getSupportFragmentManager(), "");
        });

        // fill fields if edit mode
        if (event != null) {
            openDatePicker.setText(getActivity().getString(R.string.week) + " " + event.WeekNumber + " " + getWeekString(event.time.dayOfWeek));
            title.setText(getActivity().getResources().getString(R.string.edit_new_event));
            what.setText(event.what);
            hour.setText(String.valueOf(event.time.hour));
            min.setText(String.valueOf(event.time.min));
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
                Toast.makeText(getActivity(), getContext().getString(R.string.hour_or_min_is_not_valid), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Time.Validator(Integer.valueOf(hour.getText().toString()), Integer.valueOf(min.getText().toString()))) {
                Toast.makeText(getActivity(), getContext().getString(R.string.hour_or_min_is_not_valid), Toast.LENGTH_SHORT).show();
                return;
            }

            if (what.getText().toString().equals("")) {
                Toast.makeText(getActivity(), getContext().getString(R.string.fill_blanks), Toast.LENGTH_SHORT).show();
                return;
            }

            if (weekNumber == 10) {
                Toast.makeText(getActivity(), getContext().getString(R.string.first_select_date), Toast.LENGTH_SHORT).show();
                return;
            }

            if (event == null) event = new Event();
            event.what = what.getText().toString();
            event.time = new Time(dayOfWeek, Integer.valueOf(hour.getText().toString()), Integer.valueOf(min.getText().toString()));
            event.WeekNumber = weekNumber;

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
