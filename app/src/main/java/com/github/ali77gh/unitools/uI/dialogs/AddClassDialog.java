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
import com.github.ali77gh.unitools.data.model.Time;
import com.github.ali77gh.unitools.data.model.UClass;

/**
 * Created by ali77gh on 10/12/18.
 */

public class AddClassDialog extends BaseDialog {

    private AddClassDialogListener listener;
    private Spinner daySpinner;

    public AddClassDialog(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_add_class);

        EditText lable = findViewById(R.id.text_home_add_class_dialog_lable);
        EditText where = findViewById(R.id.text_home_add_class_dialog_where);
        EditText hour = findViewById(R.id.text_home_add_class_dialog_hour);
        EditText min = findViewById(R.id.text_home_add_class_dialog_min);

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

        daySpinner = findViewById(R.id.spinner_home_add_class_day);
        SetupSpinners();

        Button cancel = findViewById(R.id.btn_home_add_class_dialog_cancel);
        Button ok = findViewById(R.id.btn_home_add_class_dialog_ok);

        ok.setOnClickListener(view -> {

            if (!IsInt(hour.getText().toString()) | !IsInt(min.getText().toString())){
                Toast.makeText(getActivity(), getContext().getString(R.string.hour_or_min_is_not_valid), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Time.Validator(Integer.valueOf(hour.getText().toString()),Integer.valueOf(min.getText().toString()))){
                Toast.makeText(getActivity(), getContext().getString(R.string.hour_or_min_is_not_valid), Toast.LENGTH_SHORT).show();
                return;
            }

            if (lable.getText().toString().equals("") | where.getText().toString().equals("") ){
                Toast.makeText(getActivity(), getContext().getString(R.string.fill_blanks), Toast.LENGTH_SHORT).show();
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
            Integer.valueOf(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}
