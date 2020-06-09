package com.tencent.qcloud.uikit.common.component.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tencent.qcloud.uikit.R;

/**
 * Created by valxehuang on 2018/7/27.
 */

public class PageTitleBar extends LinearLayout {
    public enum POSITION {
        LEFT,
        CENTER,
        RIGHT;
    }

    public LinearLayout mLeftGroup, mRightGroup;
    public TextView mLeftTitle, mRightTitle, mCenterTitle;
    public ImageView mLeftIcon, mRightIcon, mRightIcon0;

    public PageTitleBar(Context context) {
        super(context);
        init();
    }

    public PageTitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageTitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.layout_page_title_group, this);
        mLeftGroup = findViewById(R.id.page_title_left_group);
        mRightGroup = findViewById(R.id.page_title_right_group);
        mLeftTitle = findViewById(R.id.page_title_left_text);
        mRightTitle = findViewById(R.id.page_title_right_text);
        mCenterTitle = findViewById(R.id.page_title);
        mLeftIcon = findViewById(R.id.page_title_left_icon);
        mRightIcon = findViewById(R.id.page_title_right_icon);
        mRightIcon0 = findViewById(R.id.page_title_right_icon0);

    }

    public void setTitle(String title, POSITION position) {
        switch (position) {
            case LEFT:
                mLeftTitle.setText(title);
                break;
            case RIGHT:
                mRightTitle.setText(title);
                break;
            case CENTER:
                mCenterTitle.setText(title);
                break;
        }
    }

    public LinearLayout getLeftGroup() {
        return mLeftGroup;
    }

    public LinearLayout getRightGroup() {
        return mRightGroup;
    }


    public TextView getCenterTitle() {
        return mCenterTitle;
    }

    public TextView getLeftTitle() {
        return mLeftTitle;
    }

    public TextView getRightTitle() {
        return mRightTitle;
    }

    public void setLeftClick(OnClickListener clickListener) {
        mLeftGroup.setOnClickListener(clickListener);
    }

    public void setRightClick(OnClickListener clickListener) {
        mRightGroup.setOnClickListener(clickListener);
    }

    public ImageView getLeftIcon() {
        return mLeftIcon;
    }

    public ImageView getRightIcon() {
        return mRightIcon;
    }

    public ImageView getRightIcon0() {
        return mRightIcon0;
    }

}
