package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

/**
 * Created by ali77gh on 10/27/18.
 */

public class SetupWeekCounterDialog extends BaseDialog {

    public SetupWeekCounterDialog(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_setup_week_counter);

        EditText input = findViewById(R.id.edt_txt_setup_week_counter_dialog);
        Button cancel = findViewById(R.id.btn_setup_week_counter_dialog_cancel);
        Button ok = findViewById(R.id.btn_setup_week_counter_dialog_ok);

        cancel.setOnClickListener(view -> dismiss());
        int current = UserInfoRepo.getWeekNumber();
        if (current < 50)
            input.setText(String.valueOf(current));

        ok.setOnClickListener(view -> {
            if (!IsInt(input.getText().toString())) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.enter_number), Toast.LENGTH_SHORT).show();
                return;
            }
            UserInfoRepo.setWeekNumber(Integer.valueOf(input.getText().toString()));
            dismiss();
        });
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
