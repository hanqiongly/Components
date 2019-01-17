package com.jack.animator.animhelper;

import android.animation.ObjectAnimator;
import android.view.View;

public class ValueAnimatorHelper implements IAnimatorProgress{
    /**当前属性的执行的进度*/
    private float mAnimProgress;
    /**执行属性动画的目标view*/
    private View animView;

    public static ValueAnimatorHelper startAnim(View animView, long duration) {
        ValueAnimatorHelper animatorHelper = new ValueAnimatorHelper(animView);
        ObjectAnimator.ofFloat(animatorHelper, IAnimatorProgress.ANIMATOR_PROGRESS, 0, 1)
                .setDuration(duration)
                .start();
        return animatorHelper;
    }

    public ValueAnimatorHelper(View targetView) {
        animView = targetView;
    }

    @Override
    public float getAnimatorProgress() {
        return mAnimProgress;
    }

    @Override
    public void setAnimProgress(float progress) {
        mAnimProgress = progress > 1.0f ? 1.0f : progress;
        if (animView != null) {
            animView.invalidate();
        }
    }
}
