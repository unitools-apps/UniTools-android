package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.model.Event;
import com.github.ali77gh.unitools.data.model.Time;

/**
 * Created by ali77gh on 10/17/18.
 */

public class AddEventDialog extends BaseDialog {

    private Spinner daySpinner;
    private EventDialogListener listener;

    public AddEventDialog(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_event);

        EditText what = findViewById(R.id.text_home_add_event_dialog_lable);
        EditText hour = findViewById(R.id.text_home_add_event_dialog_hour);
        EditText min = findViewById(R.id.text_home_add_event_dialog_min);
        EditText week = findViewById(R.id.text_home_add_event_dialog_week_number);

        daySpinner = findViewById(R.id.spinner_home_add_event_day);
        SetupSpinners();

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

        Button cancel = findViewById(R.id.btn_home_add_event_dialog_cancel);
        Button ok = findViewById(R.id.btn_home_add_event_dialog_ok);

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

            if (!IsInt(week.getText().toString()) ||
                    Integer.valueOf(week.getText().toString()) < 0 ||
                    Integer.valueOf(week.getText().toString()) > 32) {
                Toast.makeText(getActivity(), getContext().getString(R.string.week_number_is_not_valid), Toast.LENGTH_SHORT).show();
                return;
            }

            if (what.getText().toString().equals("")) {
                Toast.makeText(getActivity(), getContext().getString(R.string.fill_blanks), Toast.LENGTH_SHORT).show();
                return;
            }

            Event event = new Event();

            event.what = what.getText().toString();
            event.time = new Time(daySpinner.getSelectedItemPosition(), Integer.valueOf(hour.getText().toString()), Integer.valueOf(min.getText().toString()));
            event.WeekNumber = Integer.valueOf(week.getText().toString());

            listener.onNewEvent(event);
            dismiss();
        });

        cancel.setOnClickListener(view -> dismiss());
    }

    private void SetupSpinners() {
        String modes[] = getContext().getResources().getStringArray(R.array.weekDays);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, modes);
        daySpinner.setAdapter(adapter);
    }

    public void setListener(EventDialogListener listener) {
        this.listener = listener;
    }

    public interface EventDialogListener {
        void onNewEvent(Event event);
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
