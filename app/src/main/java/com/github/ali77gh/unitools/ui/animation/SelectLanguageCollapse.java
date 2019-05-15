package com.github.ali77gh.unitools.ui.animation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class SelectLanguageCollapse {

    private static final int duration = 1600;
    private static int temp;

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();


        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    temp = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.getLayoutParams().height = temp;
                    v.getLayoutParams().width = temp;
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration(duration);
        v.startAnimation(a);
    }
}
