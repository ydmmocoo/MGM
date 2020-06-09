package com.fjx.mg.moments.city;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.DynamicDrawableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.library.common.utils.CircleMovementMethod;
import com.library.common.utils.ContextManager;
import com.library.common.utils.CustomImageSpan;
import com.library.common.utils.SpannableClickable;
import com.library.repository.models.MomentsInfoModel;

import java.util.List;

/**
 * Created by yiwei on 16/7/9.
 */
@SuppressLint("AppCompatCustomView")
public class DetailPraiseListView extends TextView {
    private int itemColor;
    private int itemSelectorColor;
    private List<MomentsInfoModel.PraiseListBean> datas;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public DetailPraiseListView(Context context) {
        super(context);
    }

    public DetailPraiseListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
    }

    public DetailPraiseListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = typedArray.getColor(R.styleable.PraiseListView_item_color, getResources().getColor(R.color.black_33));
            itemSelectorColor = typedArray.getColor(R.styleable.PraiseListView_item_selector_color, getResources().getColor(R.color.default_clickable_color));
        } finally {
            typedArray.recycle();
        }
    }

    public List<MomentsInfoModel.PraiseListBean> getDatas() {
        return datas;
    }

    public void setDatas(List<MomentsInfoModel.PraiseListBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        if (datas != null && datas.size() > 0) {
            //添加点赞图标
            builder.append(setImageSpan());
            MomentsInfoModel.PraiseListBean item;
            for (int i = 0; i < datas.size(); i++) {
                item = datas.get(i);
                if (item != null) {
                    builder.append(setClickableSpan(item.getUserNickName(), i));
                    if (i != datas.size() - 1) {
                        builder.append(", ");
                    }
                }
            }
        }
        setText(builder);
        setMovementMethod(new CircleMovementMethod(itemSelectorColor));
    }

    private SpannableString setImageSpan() {
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new CustomImageSpan(ContextManager.getContext(), R.drawable.like_red, DynamicDrawableSpan.ALIGN_BASELINE),
                0, 1, 2);
        return imgSpanText;
    }

    @NonNull
    private SpannableString setClickableSpan(String textStr, final int position) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(itemColor) {
                                    @Override
                                    public void onClick(View widget) {
                                        if (onItemClickListener != null) {
                                            onItemClickListener.onClick(position);
                                        }
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }
}
