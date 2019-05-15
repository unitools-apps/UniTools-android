package com.github.ali77gh.unitools.ui.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title in some android versions
    }

    protected Activity getActivity(){
        return activity;
    }
}
