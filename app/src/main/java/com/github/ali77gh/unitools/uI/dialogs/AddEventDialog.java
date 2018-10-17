package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.Model.Event;
import com.github.ali77gh.unitools.data.Model.Time;

/**
 * Created by ali77gh on 10/17/18.
 */

public class AddEventDialog extends Dialog {

    private Spinner daySpinner;
    private EventDialogListener listener;
    private Context context;

    public AddEventDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_event);

        EditText what = findViewById(R.id.text_home_add_event_dialog_lable);
        EditText hour = findViewById(R.id.text_home_add_event_dialog_hour);
        EditText min = findViewById(R.id.text_home_add_event_dialog_min);

        daySpinner = findViewById(R.id.spinner_home_add_event_day);
        SetupSpinners();

        Button cancel = findViewById(R.id.btn_home_add_event_dialog_cancel);
        Button ok = findViewById(R.id.btn_home_add_event_dialog_ok);

        ok.setOnClickListener(view -> {

            if (!IsInt(hour.getText().toString()) | !IsInt(min.getText().toString())){
                Toast.makeText(context, getContext().getString(R.string.hour_or_min_is_not_valid), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Time.Validator(Integer.valueOf(hour.getText().toString()),Integer.valueOf(min.getText().toString()))){
                Toast.makeText(context, getContext().getString(R.string.hour_or_min_is_not_valid), Toast.LENGTH_SHORT).show();
                return;
            }

            if (what.getText().toString().equals("")){
                Toast.makeText(context, getContext().getString(R.string.fill_blanks), Toast.LENGTH_SHORT).show();
                return;
            }

            Event event = new Event();

            event.what = what.getText().toString();
            event.time = new Time(daySpinner.getSelectedItemPosition() , Integer.valueOf(hour.getText().toString()) , Integer.valueOf(min.getText().toString()));


            listener.onNewEvent(event);
            dismiss();
        });

        cancel.setOnClickListener(view -> {
            dismiss();
        });
    }

    private void SetupSpinners(){
        String modes[] = getContext().getResources().getStringArray(R.array.weekDays);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, modes);
        daySpinner.setAdapter(adapter);
    }

    public void setListener(EventDialogListener listener){
        this.listener = listener;
    }

    public interface EventDialogListener{
        void onNewEvent(Event event);
    }

    private boolean IsInt(String s){
        try {
            int a = Integer.valueOf(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}
