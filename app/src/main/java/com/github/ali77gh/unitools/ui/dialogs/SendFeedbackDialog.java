package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.github.ali77gh.unitools.R;
import com.github.ali77gh.unitools.core.CH;
import com.github.ali77gh.unitools.core.onlineapi.Promise;
import com.github.ali77gh.unitools.core.onlineapi.SendFeedbackApi;

;

/**
 * Created by ali77gh on 12/11/18.
 */

public class SendFeedbackDialog extends BaseDialog {

    public SendFeedbackDialog(@NonNull Activity activity) {
        super(activity);
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
                CH.toast(R.string.enter_your_feedback);
                return;
            }

            progressBar.setVisibility(View.VISIBLE);
            send.setEnabled(false);
            send.setAlpha((float) 0.5);

            SendFeedbackApi.Send(getActivity(), input.getText().toString(), new Promise<String>() {
                @Override
                public void onFailed(String msg) {
                    CH.toast(msg);
                    progressBar.setVisibility(View.GONE);
                    send.setEnabled(true);
                    send.setAlpha(1);
                }

                @Override
                public void onSuccess(String output) {
                    CH.toast(R.string.feedback_recorded);
                    CH.toast(R.string.thank_you);
                    dismiss();
                }
            });
        });

    }
}
