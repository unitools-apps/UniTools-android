package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.Translator;
import com.github.ali77gh.unitools.data.model.Event;

/**
 * Created by ali77gh on 11/14/18.
 */

public class EventInfoDialog extends Dialog {


    private Event event;
    private OnDeleteListener listener;

    public EventInfoDialog(@NonNull Context context, Event event, OnDeleteListener listener) {
        super(context);
        this.event = event;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_event_info);
        TextView name = findViewById(R.id.txt_home_event_info_dialog_name);
        Button cancel = findViewById(R.id.btn_home_event_info_dialog_cancel);
        Button delete = findViewById(R.id.btn_home_event_info_dialog_delete);

        //load info
        name.setText(Translator.getEventReadable(event));

        //setup events
        delete.setOnClickListener(view -> {
            listener.onDelete();
            dismiss();
        });

        cancel.setOnClickListener(view -> dismiss());

    }
}
