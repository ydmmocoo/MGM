package com.library.common.utils;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;
import android.view.View;

/**
 * Created by yiang on 2018/4/10.
 */

public class GradientDrawableHelper {

    private GradientDrawable gradientDrawable;
    private View view;

    private GradientDrawableHelper(View view) {
        this.view = view;
        gradientDrawable = (GradientDrawable) view.getBackground().mutate();
    }

    public static GradientDrawableHelper whit(View view) {
        return new GradientDrawableHelper(view);
    }


    /**
     * 设置颜色
     *
     * @param colorId
     * @return
     */
    public GradientDrawableHelper setColor(int colorId) {
        gradientDrawable.setColor(ContextCompat.getColor(view.getContext(), colorId));
        return this;
    }

    /**
     * 设置颜色
     *
     * @param color
     * @return
     */
    public GradientDrawableHelper setColor(String color) {
        gradientDrawable.setColor(Color.parseColor(color));
        return this;
    }

    /**
     * 设置圆角
     *
     * @param radius
     * @return
     */
    public GradientDrawableHelper setCornerRadius(float radius) {
        gradientDrawable.setCornerRadius(radius);
        return this;
    }


    /**
     * 分别设置圆角大小
     *
     * @param topLeft
     * @param topRight
     * @param bottomLeft
     * @param bottomRight
     * @return
     */
    public GradientDrawableHelper setCornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        float[] radius = new float[]{topLeft, topLeft, topRight, topRight, bottomRight, bottomRight, bottomLeft, bottomLeft};
        gradientDrawable.setCornerRadii(radius);
        return this;
    }


    /**
     * 设置边框
     *
     * @param strokeWidth
     * @param color
     * @return
     */
    public GradientDrawableHelper setStroke(int strokeWidth, int color) {
        gradientDrawable.setStroke(strokeWidth, ContextCompat.getColor(view.getContext(), color));
        return this;
    }

    /**
     * 设置边框
     *
     * @param strokeWidth
     * @param color
     * @return
     */
    public GradientDrawableHelper setStroke(int strokeWidth, String color) {
        gradientDrawable.setStroke(strokeWidth, Color.parseColor(color));
        return this;
    }

    public GradientDrawableHelper setGradientColor(GradientDrawable.Orientation orientation, int[] color) {
        gradientDrawable.setOrientation(orientation);
        gradientDrawable.setColors(color);
        return this;
    }
}
