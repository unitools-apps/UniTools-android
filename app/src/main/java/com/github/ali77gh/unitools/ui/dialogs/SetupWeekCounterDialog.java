package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.MyDataBeen;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

import java.util.ArrayList;
import java.util.List;

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

        setContentView(R.layout.dialog_setup_week_counter);

        Spinner spinner = findViewById(R.id.spinner_txt_setup_week_counter_dialog);
        Button cancel = findViewById(R.id.btn_setup_week_counter_dialog_cancel);
        Button ok = findViewById(R.id.btn_setup_week_counter_dialog_ok);

        cancel.setOnClickListener(view -> dismiss());

        //load current
        int current = UserInfoRepo.getWeekNumber();
        SetupSpinners(spinner);
        spinner.post(() -> {
            if (current <= 18)
                spinner.setSelection(current);
            else
                spinner.setSelection(0);
        });

        ok.setOnClickListener(view -> {

            UserInfoRepo.setWeekNumber(spinner.getSelectedItemPosition());
            MyDataBeen.onWeekCounterClick();
            dismiss();
        });
    }


    private void SetupSpinners(Spinner spinner) {

        List<String> nums = new ArrayList<>();
        for (int i =0 ; i <= 18 ;i++)
            nums.add(String .valueOf(i));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.item_spinner, nums);
        spinner.setAdapter(adapter);
    }

}
