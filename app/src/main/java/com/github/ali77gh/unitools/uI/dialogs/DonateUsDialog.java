package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.github.ali77gh.unitools.R;

/**
 * Created by ali77gh on 12/11/18.
 */

public class DonateUsDialog extends Dialog {

    private Context context;

    public DonateUsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_donate_us);
    }
}
