package com.fjx.mg.widget.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * create by hanlz
 * 2019/9/23
 * Describe:
 */
public class XLoadingLayout extends FrameLayout implements ILoadingView {

    public XLoadingLayout(Context context) {
        super(context);
    }

    public XLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XLoadingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void showLoading() {
        setVisibility(VISIBLE);
    }

    @Override
    public void hideLoading() {
        setVisibility(GONE);
    }
}
