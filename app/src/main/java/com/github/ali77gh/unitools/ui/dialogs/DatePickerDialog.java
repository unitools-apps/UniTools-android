package com.github.ali77gh.unitools.ui.dialogs;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ali.uneversaldatetools.date.Calendar;
import com.ali.uneversaldatetools.date.DateSystem;
import com.ali.uneversaldatetools.datePicker.UDatePicker;
import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.model.UserInfo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

public class DatePickerDialog extends DialogFragment {

    private DatePickerDialog.DatePickerDialogListener mListener;

    public DatePickerDialog() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_u_date_picker, container);

        UDatePicker datePicker = view.findViewById(R.id.date_picker_dialog_date_picker);
        Button select = view.findViewById(R.id.btn_dialog_select);
        Button cancel = view.findViewById(R.id.btn_dialog_cancel);

        switch (UserInfoRepo.getUserInfo().Calendar) {
            case 'g':
                datePicker.ShowDatePicker(getChildFragmentManager(), Calendar.Gregorian);
                break;
            case 'j':
                datePicker.ShowDatePicker(getChildFragmentManager(), Calendar.Jalali);
                break;
            default:
                UserInfo ui = UserInfoRepo.getUserInfo();
                ui.Calendar = 'j';
                UserInfoRepo.setUserInfo(ui);
                datePicker.ShowDatePicker(getChildFragmentManager(), Calendar.Jalali);
                break;
        }

        select.setOnClickListener((v) -> {
            if (this.mListener != null) {
                this.mListener.onSelect((long) datePicker.getSelectedUnixTime(), datePicker.getSelectedDate());
            }

            this.dismiss();
        });
        cancel.setOnClickListener((v) -> {
            this.dismiss();
        });
        return view;
    }

    public void setListener(DatePickerDialog.DatePickerDialogListener listener) {
        this.mListener = listener;
    }

    public interface DatePickerDialogListener {
        void onSelect(long unixTime, DateSystem date);
    }
}
