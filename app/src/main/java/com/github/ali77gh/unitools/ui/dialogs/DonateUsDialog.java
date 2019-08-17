package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.onlineapi.DonateUsApi;

;

/**
 * Created by ali77gh on 12/11/18.
 */

public class DonateUsDialog extends BaseDialog {


    private int ONE_COFFEE_PRICE = 1000;

    public DonateUsDialog(@NonNull Activity activity) {
        super(activity);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_donate_us);

        EditText input = findViewById(R.id.text_settings_donate_dialog_input);
        Button cancel = findViewById(R.id.btn_settings_donate_dialog_cancel);
        Button donateBtn = findViewById(R.id.btn_settings_donate_dialog_send);
        TextView coffeeCalc = findViewById(R.id.text_settings_donate_dialog_coffee_calc);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String inputStr = input.getText().toString();

                if (inputStr.equals("")) coffeeCalc.setVisibility(View.GONE);
                else {
                    coffeeCalc.setVisibility(View.VISIBLE);
                    if (IsInt(inputStr)) {
                        int cost = Integer.valueOf(inputStr);
                        int coffeeCount = cost / ONE_COFFEE_PRICE;
                        coffeeCalc.setText(String.valueOf(coffeeCount) + " " + getActivity().getResources().getString(R.string.coffee));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cancel.setOnClickListener(view -> {
            dismiss();
        });

        donateBtn.setOnClickListener(view -> {
            if (!IsInt(input.getText().toString()) || input.getText().toString().equals("")) {
                CH.toast(R.string.enter_number);
                return;
            }

            DonateUsApi.OpenDonationGate(getActivity(), Integer.valueOf(input.getText().toString()));
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
