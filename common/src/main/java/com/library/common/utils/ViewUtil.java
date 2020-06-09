package com.library.common.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.library.common.R;

public class ViewUtil {
    public static void drawableRight(TextView textView, int resId) {
        if (resId == 0) {
            textView.setCompoundDrawables(null, null, null, null);
            return;
        }
        Drawable drawable = textView.getContext().getResources().getDrawable(resId);
        //注意查看方法TextView.setCompoundDrawables(Drawable, Drawable, Drawable, Drawable)
        //的注释，要求设置的drawable必须已经通过Drawable.setBounds方法设置过边界参数
        //所以，此种方式下该行必不可少
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }

    public static void setDrawableLeft(TextView view, int drawableId) {
        setDrawableLeft(view, drawableId, 0, 0);
    }

    public static void setDrawableRight(TextView view, int drawableId) {
        setDrawableRight(view, drawableId, 0, 0);
    }

    public static void setDrawableTop(TextView view, int drawableId) {
        setDrawableTop(view, drawableId, 0, 0);
    }

    public static void setDrawableBottom(TextView view, int drawableId) {
        setDrawableBottom(view, drawableId, 0, 0);
    }

    public static void setDrawableLeft(TextView view, int drawableId, int iconWith, int iconHight) {
        if (drawableId == -1) {
            view.setCompoundDrawables(null, null, null, null);
        } else {
            Context context = ContextManager.getContext();
            Drawable drawable = null;
            if (drawableId != 0) {
                drawable = context.getResources().getDrawable(drawableId);
                if (iconWith == 0) {
                    iconWith = drawable.getMinimumWidth();
                } else {
                    iconWith = DimensionUtil.dip2px(iconWith);
                }
                if (iconHight == 0) {
                    iconHight = drawable.getMinimumHeight();
                } else {
                    iconHight = DimensionUtil.dip2px(iconHight);
                }

                drawable.setBounds(0, 0, iconWith, iconHight);
            }

            view.setCompoundDrawables(drawable, null, null, null);
        }

    }

    public static void setDrawableRight(TextView view, int drawableId, int iconWith, int iconHight) {

        Context context = ContextManager.getContext();
        Drawable drawable = null;
        if (drawableId != 0) {
            drawable = context.getResources().getDrawable(drawableId);
            if (iconWith == 0) {
                iconWith = drawable.getMinimumWidth();
            } else {
                iconWith = DimensionUtil.dip2px(iconWith);
            }
            if (iconHight == 0) {
                iconHight = drawable.getMinimumHeight();
            } else {
                iconHight = DimensionUtil.dip2px(iconHight);
            }

            drawable.setBounds(0, 0, iconWith, iconHight);
        }

        view.setCompoundDrawables(null, null, drawable, null);
    }

    public static void setDrawableTop(TextView view, int drawableId, int iconWith, int iconHight) {

        Context context = ContextManager.getContext();
        Drawable drawable = null;
        if (drawableId != 0) {
            drawable = context.getResources().getDrawable(drawableId);
            if (iconWith == 0) {
                iconWith = drawable.getMinimumWidth();
            } else {
                iconWith = DimensionUtil.dip2px(iconWith);
            }
            if (iconHight == 0) {
                iconHight = drawable.getMinimumHeight();
            } else {
                iconHight = DimensionUtil.dip2px(iconHight);
            }

            drawable.setBounds(0, 0, iconWith, iconHight);
        }

        view.setCompoundDrawables(null, drawable, null, null);
    }

    public static void setDrawableBottom(TextView view, int drawableId, int iconWith, int iconHight) {

        Context context = ContextManager.getContext();
        Drawable drawable = null;
        if (drawableId != 0) {
            drawable = context.getResources().getDrawable(drawableId);
            if (iconWith == 0) {
                iconWith = drawable.getMinimumWidth();
            } else {
                iconWith = DimensionUtil.dip2px(iconWith);
            }
            if (iconHight == 0) {
                iconHight = drawable.getMinimumHeight();
            } else {
                iconHight = DimensionUtil.dip2px(iconHight);
            }

            drawable.setBounds(0, 0, iconWith, iconHight);
        }

        view.setCompoundDrawables(null, null, null, drawable);
    }

    public static void setDrawableBackGround(TextView view, int drawableId) {
        view.setBackground(ContextManager.getContext().getResources().getDrawable(drawableId));
    }

    public static void setTextColor(TextView view, int drawableId) {
        view.setTextColor(ContextManager.getContext().getResources().getColor(drawableId));
    }
}
