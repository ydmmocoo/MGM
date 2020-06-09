package com.fjx.mg.widget.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fjx.mg.R;
import com.library.common.utils.CircleMovementMethod;

/**
 * Author    by hanlz
 * Date      on 2019/11/11.
 * Description：
 */
public class ExpandTextView extends LinearLayout {
    public static final int DEFAULT_MAX_LINES = 2;//最大的行数
    private TextView contentText;
    private TextView textState;

    private int showLines;

    private ExpandStatusListener expandStatusListener;
    private LongClickListener longClickListener;
    private boolean isExpand;

    private Context context;


    public ExpandTextView(Context context) {
        super(context);
        initView(context);
    }

    public ExpandTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        initView(context);
    }

    public ExpandTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        initView(context);
    }

    private void initView(final Context context) {
        this.context = context;
        setOrientation(LinearLayout.VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_expand_text, this);
        contentText = findViewById(R.id.contentText);
        if (showLines > 0) {
            contentText.setMaxLines(showLines);
        }

        textState = findViewById(R.id.textState);
        textState.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String textStr = textState.getText().toString().trim();
                if (context.getResources().getString(R.string.show_text).equals(textStr)) {
                    contentText.setMaxLines(Integer.MAX_VALUE);
                    textState.setText(context.getResources().getString(R.string.hide_text));
                    setExpand(true);
                } else {
                    contentText.setMaxLines(showLines);
                    textState.setText(context.getResources().getString(R.string.show_text));
                    setExpand(false);
                }
                //通知外部状态已变更
                if (expandStatusListener != null) {
                    expandStatusListener.statusChange(isExpand());
                }
            }
        });
        contentText.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (longClickListener != null) {
                    longClickListener.longClick(view);
                }
                return true;
            }
        });
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.ExpandTextView, 0, 0);
        try {
            showLines = typedArray.getInt(R.styleable.ExpandTextView_showLines, DEFAULT_MAX_LINES);
        } finally {
            typedArray.recycle();
        }
    }

    public void setText(final CharSequence content) {
        contentText.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                // 避免重复监听
                contentText.getViewTreeObserver().removeOnPreDrawListener(this);

                int linCount = contentText.getLineCount();
                if (linCount > showLines) {

                    if (isExpand) {
                        contentText.setMaxLines(Integer.MAX_VALUE);
                        textState.setText(context.getResources().getString(R.string.hide_text));
                    } else {
                        contentText.setMaxLines(showLines);
                        textState.setText(context.getResources().getString(R.string.show_text));
                    }
                    textState.setVisibility(View.VISIBLE);
                } else {
                    textState.setVisibility(View.GONE);
                }
                return true;
            }


        });
        contentText.setText(content);
        /*TODO 会影响选中变色效果 2019年12月18日10:02:38 目前先隐藏 后续有问题再做修改*/
//        contentText.setMovementMethod(new CircleMovementMethod(getResources().getColor(R.color.friends_black)));
    }

    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    public boolean isExpand() {
        return this.isExpand;
    }

    public void setExpandStatusListener(ExpandStatusListener listener) {
        this.expandStatusListener = listener;
    }

    public static interface ExpandStatusListener {

        void statusChange(boolean isExpand);
    }

    public void setLongClickListener(LongClickListener listener) {
        this.longClickListener = listener;
    }

    public interface LongClickListener {

        void longClick(View view);
    }
}

