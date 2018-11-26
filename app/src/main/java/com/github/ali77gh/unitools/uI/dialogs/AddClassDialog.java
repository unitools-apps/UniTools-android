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
import com.github.ali77gh.unitools.data.model.Time;
import com.github.ali77gh.unitools.data.model.UClass;

/**
 * Created by ali77gh on 10/12/18.
 */

public class AddClassDialog extends Dialog {

    private AddClassDialogListener listener;
    private Spinner daySpinner;
    private Context context;

    public AddClassDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_add_class);

        EditText lable = findViewById(R.id.text_home_add_class_dialog_lable);
        EditText where = findViewById(R.id.text_home_add_class_dialog_where);
        EditText hour = findViewById(R.id.text_home_add_class_dialog_hour);
        EditText min = findViewById(R.id.text_home_add_class_dialog_min);

        daySpinner = findViewById(R.id.spinner_home_add_class_day);
        SetupSpinners();

        Button cancel = findViewById(R.id.btn_home_add_class_dialog_cancel);
        Button ok = findViewById(R.id.btn_home_add_class_dialog_ok);

        ok.setOnClickListener(view -> {

            if (!IsInt(hour.getText().toString()) | !IsInt(min.getText().toString())){
                Toast.makeText(context, getContext().getString(R.string.hour_or_min_is_not_valid), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Time.Validator(Integer.valueOf(hour.getText().toString()),Integer.valueOf(min.getText().toString()))){
                Toast.makeText(context, getContext().getString(R.string.hour_or_min_is_not_valid), Toast.LENGTH_SHORT).show();
                return;
            }

            if (lable.getText().toString().equals("") | where.getText().toString().equals("") ){
                Toast.makeText(context, getContext().getString(R.string.fill_blanks), Toast.LENGTH_SHORT).show();
                return;
            }

            UClass uClass = new UClass();

            uClass.where = where.getText().toString();
            uClass.what = lable.getText().toString();
            uClass.time = new Time(daySpinner.getSelectedItemPosition(),Integer.valueOf(hour.getText().toString()),Integer.valueOf(min.getText().toString()));

            listener.onNewClass(uClass);
            dismiss();
        });

        cancel.setOnClickListener(view -> {
            dismiss();
        });
    }

    public void setListener(AddClassDialogListener listener) {
        this.listener = listener;
    }

    private void SetupSpinners(){
        String modes[] = getContext().getResources().getStringArray(R.array.weekDays);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, modes);
        daySpinner.setAdapter(adapter);
    }

    public interface AddClassDialogListener {
        void onNewClass(UClass uClass);
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
