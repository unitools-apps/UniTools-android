package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.data.model.Event;
import com.github.ali77gh.unitools.data.repo.EventRepo;

/**
 * Created by ali77gh on 11/14/18.
 */

public class EventInfoDialog extends BaseDialog {


    private Event event;
    private OnDeleteListener deleteListener;
    private AddEventDialog.EventDialogListener editListener;

    public EventInfoDialog(@NonNull Activity activity, Event event, OnDeleteListener listener) {
        super(activity);
        this.event = event;
        this.deleteListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_event_info);
        ImageView edit = findViewById(R.id.image_home_event_info_dialog_edit);
        TextView name = findViewById(R.id.txt_home_event_info_dialog_name);
        Button cancel = findViewById(R.id.btn_home_event_info_dialog_cancel);
        Button delete = findViewById(R.id.btn_home_event_info_dialog_delete);
        EditText describe = findViewById(R.id.text_home_event_info_dialog_describe);

        //load info
        name.setText(Translator.getEventReadable(event));
        describe.setText(event.describe);

        //setup events
        delete.setOnClickListener(view -> {
            deleteListener.onDelete();
            dismiss();
        });

        cancel.setOnClickListener(view ->{
            if (!describe.getText().toString().equals(event.describe)){
                event.describe = describe.getText().toString();
                EventRepo.Update(event);
            }
            dismiss();
        });

        edit.setOnClickListener(view -> {
            AddEventDialog addFriendDialog = new AddEventDialog(getActivity(), event);
            addFriendDialog.setListener(event2 -> {
                event2.id = event.id;
                editListener.onNewEvent(event2);
                name.setText(Translator.getEventReadable(event2));
            });
            addFriendDialog.show();
            CH.toast(R.string.enter_time_in_24_system);
        });

    }

    public void setEditListener(AddEventDialog.EventDialogListener editListener) {
        this.editListener = editListener;
    }
}
