package com.github.ali77gh.unitools.ui.animation;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by x1 carbon on 3/14/2018.
 */

public class ExpandAndCollapse {

    private static final int duration = 200;

    public static void expand(final View v) {
        expand(v,true);
    }

    public static void expand(final View v,boolean fade) {
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setDuration(duration);
        v.startAnimation(a);

        //alpha
        if (fade){
            v.setAlpha(0);
            v.animate().alpha(1).setDuration(duration).start();
        }
    }

    public static void collapse(final View v) {
        collapse(v,true);
    }

    public static void collapse(final View v,boolean fade) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
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

        //alpha
        if (fade){
            v.setAlpha(1);
            v.animate().alpha(0).setDuration(duration).start();
        }

    }
}
