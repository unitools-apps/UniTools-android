package com.github.ali77gh.unitools.ui.animation;

import android.view.View;

public class FadeLoop {

    private int duration = 300;
    private float minFade = 0;
    private float maxFade = 1;

    private int counter = 1;
    private boolean forceStoped = false;
    private int loop;
    private View view;

    public FadeLoop(View view,int loop,int duration){

        if (loop < 1) throw new RuntimeException("loop should be more then 1 current:" + String.valueOf(loop));
        this.view = view;
        this.loop = loop;
        this.duration = duration;
    }

    public FadeLoop(View view,int loop){

        if (loop < 1) throw new RuntimeException("loop should be more then 1 current:" + String.valueOf(loop));
        this.view = view;
        this.loop = loop;
    }

    public void setMinFade(float minFade) {
        this.minFade = minFade;
    }

    public void setMaxFade(float maxFade) {
        this.maxFade = maxFade;
    }

    public void animate(){
        OneTimeAnim(view,animationEnd);
    }

    public void stopAnimate() {
        forceStoped = true;
    }

    private OnAnimationEnd animationEnd = new OnAnimationEnd() {
        @Override
        public void onAnimationEnd() {
            if (loop == counter) return;
            if (forceStoped) return;
            counter++;
            OneTimeAnim(view,animationEnd);
        }
    };

    private void OneTimeAnim(View view, OnAnimationEnd onAnimationEnd){

        view.animate().alpha(minFade).setDuration(duration).start();

        view.postDelayed(() -> view.animate().alpha(maxFade).setDuration(duration).start(),duration+50);

        view.postDelayed(onAnimationEnd::onAnimationEnd, (duration * 2) + 100);
    }

    private interface OnAnimationEnd{
        void onAnimationEnd();
    }
}
