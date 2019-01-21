package com.github.ali77gh.unitools.uI.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.support.annotation.NonNull;

import com.github.ali77gh.unitools.R;

/**
 * Created by ali77gh on 1/20/19.
 */

class BaseDialog extends Dialog {

    private Activity activity;

    BaseDialog(@NonNull Activity activity) {
        super(activity);
        this.activity =activity;
        try {
            getWindow().getAttributes().windowAnimations = R.style.DialogAnim;
        }catch (NullPointerException ignored) {}
    }

    protected Activity getActivity(){
        return activity;
    }
}
