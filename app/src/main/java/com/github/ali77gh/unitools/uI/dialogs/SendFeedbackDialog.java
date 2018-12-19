package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.onlineapi.Promise;
import com.github.ali77gh.unitools.core.onlineapi.SendFeedbackApi;

/**
 * Created by ali77gh on 12/11/18.
 */

public class SendFeedbackDialog extends Dialog {


    private Activity activity;

    public SendFeedbackDialog(@NonNull Context context) {
        super(context);
        this.activity = (Activity) context;
        try {
            getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        }catch (NullPointerException ignored) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_send_feedback);

        EditText input = findViewById(R.id.text_settings_feedback_dialog_input);
        Button cancel = findViewById(R.id.btn_settings_feedback_dialog_cancel);
        Button send = findViewById(R.id.btn_settings_feedback_dialog_send);

        ProgressBar progressBar = findViewById(R.id.progress_settings_feedback_dialog);


        cancel.setOnClickListener(view -> dismiss());

        send.setOnClickListener(view -> {

            if (input.getText().toString().equals("")){
                Toast.makeText(activity, activity.getString(R.string.enter_your_feedback), Toast.LENGTH_SHORT).show();
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            send.setEnabled(false);
            send.setAlpha((float) 0.5);

            SendFeedbackApi.Send(activity, input.getText().toString(), new Promise<String>() {
                @Override
                public void onFailed(String msg) {
                    Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    send.setEnabled(true);
                    send.setAlpha(1);
                }

                @Override
                public void onSuccess(String output) {
                    Toast.makeText(activity, activity.getString(R.string.feedback_recorded), Toast.LENGTH_SHORT).show();
                    Toast.makeText(activity, activity.getString(R.string.thank_you), Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            });
        });

    }
}
