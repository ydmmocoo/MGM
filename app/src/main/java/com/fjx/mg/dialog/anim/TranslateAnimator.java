package com.fjx.mg.dialog.anim;


import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.animator.PopupAnimator;

public class TranslateAnimator extends PopupAnimator {
    //动画起始坐标
    private float startTranslationX, startTranslationY;
    private int oldWidth, oldHeight;


    @Override
    public void initAnimator() {
        targetView.setTranslationY(- targetView.getBottom());
        // 设置起始坐标
        startTranslationX = targetView.getTranslationX();
        startTranslationY = targetView.getTranslationY();
        oldWidth = targetView.getMeasuredWidth();
        oldHeight = targetView.getMeasuredHeight();

    }

    @Override
    public void animateShow() {

        targetView.animate().translationX(0).translationY(0)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(XPopup.getAnimationDuration()).start();
    }

    @Override
    public void animateDismiss() {
        startTranslationY -= targetView.getMeasuredHeight() - oldHeight;
        targetView.animate().translationX(startTranslationX).translationY(startTranslationY)
                .setInterpolator(new FastOutSlowInInterpolator())
                .setDuration(XPopup.getAnimationDuration()).start();

    }
}
