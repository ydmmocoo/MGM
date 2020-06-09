package com.fjx.mg.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.fjx.mg.R;

public class LevelProgress extends View {

    private Paint progressBgPaint;
    private Paint progressUpperPaint;

    private Paint imageBgPaint;
    private Paint textPaint;
    private Paint textHintPaint;

    private Bitmap imageBitmap;
    private int mViewWidth, mViewHeight;
    private int progressHeight = dip2px(getContext(), 8);

    private int padding = 70;

    private int level=1, inviteProgress;

    private String[] textLevels = new String[]{
            getContext().getString(R.string.level1),
            getContext().getString(R.string.level2),
            getContext().getString(R.string.level3),
            getContext().getString(R.string.level4),
            getContext().getString(R.string.level5)
    };
    private String[] textLevelHints = new String[]{
            getContext().getString(R.string.level_hint1),
            getContext().getString(R.string.level_hint2),
            getContext().getString(R.string.level_hint3),
            getContext().getString(R.string.level_hint4),
            getContext().getString(R.string.level_hint5),
    };


    public LevelProgress(Context context) {
        super(context);
        init(context);
    }

    public LevelProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        progressBgPaint = new Paint();
        progressBgPaint.setStrokeWidth(progressHeight);
        progressBgPaint.setAntiAlias(true);
        progressBgPaint.setStrokeCap(Paint.Cap.ROUND);
        progressBgPaint.setColor(Color.parseColor("#C0272D"));


        progressUpperPaint = new Paint();
        progressUpperPaint.setStrokeWidth(progressHeight);
        progressUpperPaint.setStrokeCap(Paint.Cap.ROUND);
        progressUpperPaint.setAntiAlias(true);
        progressUpperPaint.setColor(Color.parseColor("#f6921e"));


        imageBgPaint = new Paint();
        imageBgPaint.setAntiAlias(true);
        imageBgPaint.setColorFilter(new PorterDuffColorFilter(Color.parseColor("#C0272D"), PorterDuff.Mode.SRC_IN));
        imageBgPaint.setColor(Color.parseColor("#C0272D"));

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(1);
        textPaint.setTextSize(dip2px(context, 14));
        textPaint.setColor(Color.WHITE);

        textHintPaint = new Paint();
        textHintPaint.setAntiAlias(true);
        textHintPaint.setStrokeWidth(1);
        textHintPaint.setTextSize(dip2px(context, 10));
        textHintPaint.setColor(Color.WHITE);


        imageBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.level_progress);


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = measureWidth(widthMeasureSpec);
        mViewHeight = measureHeight(heightMeasureSpec);
        setMeasuredDimension(mViewWidth, mViewHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(padding, mViewHeight / 2f, mViewWidth - padding, mViewHeight / 2f, progressBgPaint);


        for (int i = 0; i < 5; i++) {
            canvas.drawBitmap(imageBitmap, padding + (mViewWidth - padding * 2) * i / 4f - imageBitmap.getWidth() / 2f, mViewHeight / 2f - imageBitmap.getHeight() / 2f, imageBgPaint);

            float textWidth = getTextWidth(textLevels[i]);
//            float texthintWidth = getTextHintWidth(textLevelHints[i]);
            canvas.drawText(textLevels[i], padding + (mViewWidth - padding * 2) * i / 4f - textWidth / 2, mViewHeight / 2f + dip2px(getContext(), 30), textPaint);
//            canvas.drawText(textLevelHints[i], padding + (mViewWidth - padding * 2) * i / 4f - texthintWidth / 2, mViewHeight / 3f + dip2px(getContext(), 44), textHintPaint);
        }

        canvas.drawLine(padding, mViewHeight / 2f, padding + (mViewWidth - padding * 2) * (level - 1) / 4f, mViewHeight / 2f, progressUpperPaint);

        for (int i = 0; i < level; i++) {
            canvas.drawBitmap(imageBitmap, padding + (mViewWidth - padding * 2) * i / 4f + -imageBitmap.getWidth() / 2f, mViewHeight / 2f - imageBitmap.getHeight() / 2f, null);
        }
    }


    private int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
//                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = Math.max(result, specSize);
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                result = Math.max(result, specSize);
                break;
        }
        return result;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private float getTextWidth(String text) {
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);//用一个矩形去"套"字符串,获得能完全套住字符串的最小矩形
        float width = rect.width();//字符串的高度
        return width;
    }

    private float getTextHintWidth(String text) {
        Rect rect = new Rect();
        textHintPaint.getTextBounds(text, 0, text.length(), rect);//用一个矩形去"套"字符串,获得能完全套住字符串的最小矩形
        float width = rect.width();//字符串的高度
        return width;
    }

    public void setData(int level) {
        this.level = level;
        invalidate();

    }
}
