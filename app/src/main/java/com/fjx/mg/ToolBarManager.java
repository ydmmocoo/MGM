package com.fjx.mg;

import android.app.Activity;

import androidx.annotation.ColorRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by 15 on 2017/10/12.
 * <p>
 * 公共顶部标题栏的设置管理类,适合简单的显示和点击的顶部标题栏，
 * 如果是复杂的逻辑，不要用管理器，有的界面逻辑比较复杂，用这个可能不好操作
 */

public class ToolBarManager {

    private static final String TAG = "ToolBarManager";
    private final View mContent;
    private Toolbar toolbar;
    private Activity activity;

    private TextView tvTitle;//标题
    private TextView tvRightText;
    private TextView tvRightText1;
    private ImageView ivBack;
    private ImageView ivRightIcon;

    /**
     * activity中使用
     *
     * @param activity
     */
    private ToolBarManager(Activity activity) {
        this.activity = activity;
        //获取界面内容视图
        mContent = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        initToolbar();
    }

    /**
     * 在fragment中使用
     *
     * @param activity fragment所在界面
     * @param view     fragment的视图
     * @return
     */
    private ToolBarManager(Activity activity, View view) {
        this.activity = activity;
        //获取界面内容视图
        mContent = view;
        initToolbar();
    }

    /**
     * 时间：2017/7/28
     * 描述:初始化
     */
    private void initToolbar() {
        toolbar = mContent.findViewById(R.id.id_toolbar);
        if (toolbar == null) {
            throw new NullPointerException("toolbar对象为空，请查看界面布局中是否引用layout_toolbar");
        }
        tvTitle = toolbar.findViewById(R.id.toolbar_tv_title);
        tvRightText = toolbar.findViewById(R.id.toolbar_tv_right);
        tvRightText1 = toolbar.findViewById(R.id.toolbar_tv_right1);
        ivRightIcon = toolbar.findViewById(R.id.toolbar_iv_right);
        ivBack = toolbar.findViewById(R.id.toolbar_iv_back);

        toolbar.setTitle("");//不显示toolbar自带的图标
        setNavigationIcon(true);
        toolbar.setNavigationIcon(null);
    }

    /**
     * activity中使用
     *
     * @param activity
     * @return
     */
    public static ToolBarManager with(AppCompatActivity activity) {
        return new ToolBarManager(activity);
    }

    /**
     * 在fragment中使用
     *
     * @param activity fragment所在界面
     * @param view     fragment的视图
     * @return
     */
    public static ToolBarManager with(FragmentActivity activity, View view) {
        return new ToolBarManager(activity, view);
    }

    /**
     * 时间：2017/7/28
     * 描述:设置toolbar的背景颜色
     */
    public ToolBarManager setBackgroundColor(@ColorRes int color) {
        if (color == 0) {
            toolbar.setBackgroundColor(0);
            return this;
        }
        toolbar.setBackgroundColor(ContextCompat.getColor(activity, color));
        return this;

    }

    /**
     * 时间：2017/7/28
     * 描述:设置toolbar左边的返回按钮，输入0标识不显示图标
     */
    public ToolBarManager setNavigationIcon(boolean isShow) {
        if (isShow)
            ivBack.setVisibility(View.VISIBLE);
        else
            ivBack.setVisibility(View.GONE);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        return this;
    }

    /**
     * 时间：2017/7/28
     * 描述:设置toolbar左边的返回按钮，输入0标识不显示图标
     */
    public ToolBarManager setNavigationIcon(int drawableId) {

        ivBack.setImageResource(drawableId);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
        return this;
    }

    /**
     * 时间：2017/7/28
     * 描述:设置标题内
     */
    public ToolBarManager setTitle(CharSequence title) {
        return setTitle(title, R.color.textColor);
    }

    /**
     * 时间：2017/7/28
     * 描述:设置标题内容和字体颜色
     */
    public ToolBarManager setTitle(CharSequence title, @ColorRes int color) {
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
        tvTitle.setTextColor(ContextCompat.getColor(activity, color));
        return this;
    }

    public ImageView setRightImage(int resId, View.OnClickListener listener) {
        ivRightIcon.setImageResource(resId);
        ivRightIcon.setVisibility(View.VISIBLE);
        if (listener != null)
            ivRightIcon.setOnClickListener(listener);
        return ivRightIcon;
    }

    public TextView setRightText(String text, View.OnClickListener listener) {
        tvRightText.setText(text);
        tvRightText.setVisibility(View.VISIBLE);
        if (listener != null)
            tvRightText.setOnClickListener(listener);
        return tvRightText;
    }

    public TextView setRightText(String text, @ColorRes int color, View.OnClickListener listener) {
        tvRightText.setText(text);
        tvRightText.setTextColor(ContextCompat.getColor(activity, color));
        tvRightText.setVisibility(View.VISIBLE);
        if (listener != null)
            tvRightText.setOnClickListener(listener);
        return tvRightText;
    }

    public TextView setRightTextBack(String text, View.OnClickListener listener) {
        tvRightText1.setText(text);
        tvRightText1.setVisibility(View.VISIBLE);
        if (listener != null)
            tvRightText1.setOnClickListener(listener);
        return tvRightText1;
    }

    public TextView getTitleTextView() {
        return tvTitle;
    }


    public int getToolbarHight() {
        if (toolbar == null) return 0;
        return toolbar.getHeight();
    }


}