package com.fjx.mg.moments.city;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.library.common.utils.CircleMovementMethod;
import com.library.common.utils.ContextManager;
import com.library.common.utils.CustomImageSpan;
import com.library.common.utils.SpannableClickable;
import com.library.common.utils.UrlUtils;
import com.library.repository.models.MomentsReplyListModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiwei on 16/7/9.
 */
public class DetailCommentListView extends LinearLayout {
    private int itemColor;
    private int itemSelectorColor;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemSpannableClickListener onItemSpannableClickListener;
    private List<MomentsReplyListModel.ReplyListBean> mDatas;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public void setOnItemSpannableClickListener(OnItemSpannableClickListener onItemSpannableClickListener) {
        this.onItemSpannableClickListener = onItemSpannableClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public void setDatas(List<MomentsReplyListModel.ReplyListBean> datas) {
        if (datas == null) {
            datas = new ArrayList<>();
        }
        mDatas = datas;
        notifyDataSetChanged();
    }

    public List<MomentsReplyListModel.ReplyListBean> getDatas() {
        return mDatas;
    }

    public DetailCommentListView(Context context) {
        super(context);
    }

    public DetailCommentListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs, context);
    }

    public DetailCommentListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs, context);
    }

    protected void initAttrs(AttributeSet attrs, Context context) {
        this.mContext = context;
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PraiseListView, 0, 0);
        try {
            //textview的默认颜色
            itemColor = typedArray.getColor(R.styleable.PraiseListView_item_color, getResources().getColor(R.color.black_33));
            itemSelectorColor = typedArray.getColor(R.styleable.PraiseListView_item_selector_color, getResources().getColor(R.color.default_clickable_color));

        } finally {
            typedArray.recycle();
        }
    }

    public void notifyDataSetChanged() {

        removeAllViews();
        if (mDatas == null || mDatas.size() == 0) {
            return;
        }
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i = 0; i < mDatas.size(); i++) {
            View view = getView(i);
            if (view == null) {
                throw new NullPointerException("listview item layout is null, please check getView()...");
            }

            addView(view, i, layoutParams);
        }

    }

    private View getView(final int position) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(getContext());
        }
        View convertView = layoutInflater.inflate(R.layout.item_comments, null, false);

        TextView commentTv = convertView.findViewById(R.id.commentTv);
        final CircleMovementMethod circleMovementMethod = new CircleMovementMethod(itemSelectorColor, itemSelectorColor);

        final MomentsReplyListModel.ReplyListBean bean = mDatas.get(position);
        String name = bean.getUserNickName();
        String toReplyName = "";
        if (bean.getToReplyCont() != null) {
            toReplyName = bean.getToUserNick();
        }

        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(setImageSpan(position));
        builder.append(setClickableSpan(name, bean.getUserIdfer(), bean.getUserAvatar()));

        if (!TextUtils.isEmpty(toReplyName)) {

            builder.append(" ".concat(mContext.getString(R.string.reply).concat(" ")));
            builder.append(setClickableSpan(toReplyName, bean.getToUserIdfer(), bean.getUserAvatar()));
        }
        builder.append(": ");
        //转换表情字符
        String contentBodyStr = bean.getContent();
        builder.append(UrlUtils.formatUrlString(contentBodyStr));
        commentTv.setText(builder);

        commentTv.setMovementMethod(circleMovementMethod);
        commentTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(position);
                    }
                }
            }
        });
        commentTv.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (circleMovementMethod.isPassToTv()) {
                    if (onItemLongClickListener != null) {
                        onItemLongClickListener.onItemLongClick(position);
                    }
                    return true;
                }
                return false;
            }
        });

        return convertView;
    }

    private SpannableString setImageSpan(int position) {
        String text = "  ";
        SpannableString imgSpanText = new SpannableString(text);
        imgSpanText.setSpan(new CustomImageSpan(ContextManager.getContext(), position == 0 ? R.drawable.comments_red : R.drawable.comments_transparent, 2),
                0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return imgSpanText;
    }

    @NonNull
    private SpannableString setClickableSpan(final String textStr, final String userIdfer, final String userAvatar) {
        SpannableString subjectSpanText = new SpannableString(textStr);
        subjectSpanText.setSpan(new SpannableClickable(itemColor) {
                                    @Override
                                    public void onClick(View widget) {
                                        if (onItemClickListener != null) {
                                            onItemSpannableClickListener.onItemSpannableClick(userIdfer, userAvatar);
                                        }
                                    }
                                }, 0, subjectSpanText.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return subjectSpanText;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public interface OnItemSpannableClickListener {
        void onItemSpannableClick(String userIdfer, String userAvatar);
    }


}
