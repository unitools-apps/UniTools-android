package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.data.model.Event;
import com.github.ali77gh.unitools.data.repo.EventRepo;
import com.github.ali77gh.unitools.data.repo.UserInfoRepo;

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

        EditText input = findViewById(R.id.edt_txt_setup_week_counter_dialog);
        Button cancel = findViewById(R.id.btn_setup_week_counter_dialog_cancel);
        Button ok = findViewById(R.id.btn_setup_week_counter_dialog_ok);

        cancel.setOnClickListener(view -> dismiss());

        //load current
        int current = UserInfoRepo.getWeekNumber();
        if (current < 38)
            input.setText(String.valueOf(current));

        ok.setOnClickListener(view -> {
            if (!IsInt(input.getText().toString())) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.enter_number), Toast.LENGTH_SHORT).show();
                return;
            }
            int selectedWeek = Integer.valueOf(input.getText().toString());
            if (selectedWeek > 38) {
                Toast.makeText(getActivity(), getActivity().getString(R.string.plz_enter_right_number), Toast.LENGTH_LONG).show();
                return;
            }
            UserInfoRepo.setWeekNumber(selectedWeek);

            MoveEvents(selectedWeek - current);

            dismiss();
        });
    }

    private void MoveEvents(int moveForward){
        List<Event> events = EventRepo.getAll();

        for (Event e : events){
            e.WeekNumber += moveForward;
            EventRepo.Update(e);
        }
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
