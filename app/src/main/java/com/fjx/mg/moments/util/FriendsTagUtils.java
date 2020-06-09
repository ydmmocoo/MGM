package com.fjx.mg.moments.util;

import android.view.ViewGroup;
import android.widget.TextView;

import com.fjx.mg.R;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.library.common.utils.ViewUtil;

import java.util.Random;

/**
 * create by hanlz
 * 2019/9/23
 * Describe:朋友圈标签帮助类
 */
public class FriendsTagUtils {


    public static final int BLUE = 0;
    public static final int RED = 1;
    public static final int GREEN = 2;
    public static final int YELLOW = 3;

    public FriendsTagUtils() {
    }

    public void setTextView(String tag, TextView tvTags) {

        int randomNum = new Random().nextInt(4);
        int drawableIds = -1;
        int textColorIds = -1;
        switch (randomNum) {
            case BLUE:
                drawableIds = R.drawable.friends_tv_blue_bg;
                textColorIds = R.color.friends_blue;
                break;
            case RED:
                drawableIds = R.drawable.friends_tv_red_bg;
                textColorIds = R.color.friends_red;
                break;
            case GREEN:
                drawableIds = R.drawable.friends_tv_green_bg;
                textColorIds = R.color.friends_green;
                break;
            case YELLOW:
                drawableIds = R.drawable.friends_tv_yellow_bg;
                textColorIds = R.color.friends_yellow;
                break;
            default:
                drawableIds = R.drawable.friends_tv_blue_bg;
                textColorIds = R.color.friends_blue;
                break;
        }
        ViewUtil.setDrawableBackGround(tvTags, drawableIds);
        ViewUtil.setTextColor(tvTags, textColorIds);
        tvTags.setText(tag);
        ViewGroup.LayoutParams lp = tvTags.getLayoutParams();
        if (lp instanceof FlexboxLayoutManager.LayoutParams) {
            FlexboxLayoutManager.LayoutParams flexboxLp = (FlexboxLayoutManager.LayoutParams) lp;
            flexboxLp.setFlexGrow(5.0f);
        }
    }

}
