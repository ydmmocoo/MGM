package com.fjx.mg.widget.recyclerview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;

/**
 * Author    by hanlz
 * Date      on 2019/10/29.
 * Description：
 */
public class LinearManagerItemDecaration extends RecyclerView.ItemDecoration {

    private int mSpanSpace;
    private int mOrientation;
    private Paint mPaint;

    public LinearManagerItemDecaration(int mSpanSpace, int mOrientation) {
        this.mSpanSpace = mSpanSpace;
        this.mOrientation = mOrientation;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        ;
    }

    public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

    public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            //<!--垂直方向 ，最后一个不设置padding-->
            if (parent.getChildAdapterPosition(view) < parent.getAdapter().getItemCount()) {
                outRect.set(0, 0, 0, mSpanSpace);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        } else {
            //<!--水平方向 ，最后一个不设置padding-->
            if (parent.getChildAdapterPosition(view) < parent.getAdapter().getItemCount()) {
                outRect.set(0, 0, mSpanSpace, 0);
            } else {
                outRect.set(0, 0, 0, 0);
            }
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (mOrientation == VERTICAL_LIST) {
            drawVertical(c, parent);
        } else {

        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {

        mPaint.setColor(parent.getResources().getColor(R.color.divider_color));

        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            RecyclerView v = new RecyclerView(parent.getContext());
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            final int bottom = top + mSpanSpace;
            RectF rectF = new RectF(left, top, right, bottom);
            c.drawRect(rectF, mPaint);
        }
    }
}
