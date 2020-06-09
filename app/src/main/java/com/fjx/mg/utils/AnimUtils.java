package com.fjx.mg.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Path;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fjx.mg.R;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.library.common.utils.DimensionUtil;

/**
 * @author yedeman
 * @date 2020/6/8.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class AnimUtils {
    public static void AddToShopAnim(View fromView, View ToView,
                                     Context context, final RelativeLayout mainView) {
        int[] fromLoc = new int[2];
        int[] ToLoc = new int[2];
        fromView.getLocationInWindow(fromLoc);//获取起始控件在其父窗口中的坐标位置
        ToView.getLocationInWindow(ToLoc);//获取结束控件在其父窗口中的坐标位置
        //绘制移动的路径 方便后面ViewAnimator中引用
        Path path = new Path();
        path.moveTo(fromLoc[0], fromLoc[1]);
        path.quadTo(ToLoc[0], fromLoc[1], ToLoc[0]+100, ToLoc[1]);
        //创建移动的控件
        final TextView textView = new TextView(context);
        textView.setBackgroundResource(R.drawable.circle_yellow);
        textView.setText("1");
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        //将创建好的控件添加到主界面中
        RelativeLayout.LayoutParams lp =
                new RelativeLayout.LayoutParams(DimensionUtil.dip2px(20),DimensionUtil.dip2px(20));
        mainView.addView(textView,lp);
        //ViewAnimator动画的启动实现
        ViewAnimator.animate(textView).path(path).alpha(250,0).rotation(-360)
                .accelerate().duration(500).onStop(new AnimationListener.Stop() {
            @Override
            public void onStop() {
                mainView.removeView(textView);
            }
        }).start();
    }
}
