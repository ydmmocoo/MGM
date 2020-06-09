package com.fjx.mg.widget.textview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fjx.mg.R;

/**
 * create by hanlz
 * 2019/9/23
 * Describe:折叠文本空间 可以设置最大显示行数 类似微信朋友圈展开全文
 */
public class ShowMoreTextView extends LinearLayout implements View.OnClickListener {

    private static final int COLLAPSIBLE_STATE_NONE = 0;// 不显示
    private static final int COLLAPSIBLE_STATE_SHRINKUP = 1;// 显示收起
    private static final int COLLAPSIBLE_STATE_SPREAD = 2;// 显示全文
    private int mState = COLLAPSIBLE_STATE_SPREAD;
    private static final String COLLAPSIBLE_STATE_SHRINKUP_TEXT = "收起";
    private static final String COLLAPSIBLE_STATE_SPREAD_TEXT = "全文";
    private TextView mText;

    /**
     * @return Returns the mText.
     */
    public TextView getmText() {
        return mText;
    }

    public int getmState() {
        return mState;
    }

    public void setmState(int mState) {
        this.mState = mState;
    }

    private TextView mTextTip;
    private changeState changeStateCallBack;
    private boolean isNeedLayout;
    private int maxLineCount = 3;
    private final Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            if (mText.getLineCount() <= maxLineCount) {
                // 行数不足不做处理
                mState = COLLAPSIBLE_STATE_NONE;
                mText.setMaxLines(Integer.MAX_VALUE);
                mTextTip.setVisibility(View.GONE);
            } else {
                switch (mState) {
                    case COLLAPSIBLE_STATE_SPREAD:
                        // 全文状态
                        mText.setMaxLines(maxLineCount);
                        mTextTip.setVisibility(View.VISIBLE);
                        mTextTip.setText(COLLAPSIBLE_STATE_SPREAD_TEXT);
                        break;
                    case COLLAPSIBLE_STATE_SHRINKUP:
                        // 收起状态
                        mText.setMaxLines(Integer.MAX_VALUE);
                        mTextTip.setVisibility(View.VISIBLE);
                        mTextTip.setText(COLLAPSIBLE_STATE_SHRINKUP_TEXT);
                        break;
                    default:
                        // 除非发生不可知状态，一般不会执行到这个
                        mState = COLLAPSIBLE_STATE_NONE;
                        mText.setMaxLines(Integer.MAX_VALUE);
                        mTextTip.setVisibility(View.GONE);
                        break;
                }
            }
        }
    };

    public ShowMoreTextView(Context context) {
        this(context, null);
        initView();
    }

    public ShowMoreTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    @SuppressLint("NewApi")
    public ShowMoreTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.showmore, this);
        view.setPadding(0, -1, 0, 0);
        mText = (TextView) view.findViewById(R.id.desc_tv);
        mTextTip = (TextView) view.findViewById(R.id.desc_op_tv);
        mTextTip.setOnClickListener(this);
    }

    /**
     * 设置文本
     *
     * @param charSequence
     * @param bufferType
     */
    public final void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        isNeedLayout = true;
        mState = COLLAPSIBLE_STATE_SPREAD;
        mText.setText(charSequence, bufferType);
    }

    /**
     * 设置文本
     *
     * @param charSequence
     */
    public final void setText(CharSequence charSequence) {
        isNeedLayout = true;
        mText.setText(charSequence);
    }

    @Override
    public void onClick(View v) {
        isNeedLayout = true;
        if (mState == COLLAPSIBLE_STATE_SPREAD) {
            // 如果是全文状态，就改成收起状态
            mState = COLLAPSIBLE_STATE_SHRINKUP;
            requestLayout();
        } else if (mState == COLLAPSIBLE_STATE_SHRINKUP) {
            // 如果是收起状态，就改成全文状态
            mState = COLLAPSIBLE_STATE_SPREAD;
            requestLayout();
        }
        if (null != changeStateCallBack) {
            changeStateCallBack.changeFlag(v);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (isNeedLayout) {
            isNeedLayout = false;
            handler.sendMessage(Message.obtain());
        }
    }

    public int getMaxLineCount() {
        return maxLineCount;
    }

    public void setMaxLineCount(int maxLineCount) {
        this.maxLineCount = maxLineCount;
    }

    public changeState getChangeStateCallBack() {
        return changeStateCallBack;
    }

    public void setChangeStateCallBack(changeState changeStateCallBack) {
        this.changeStateCallBack = changeStateCallBack;
    }

    public interface changeState {
        void changeFlag(View v);
    }

    public void removeCallBack() {
        if (handler != null) {
            handler.removeCallbacks(null);
        }
    }
}
