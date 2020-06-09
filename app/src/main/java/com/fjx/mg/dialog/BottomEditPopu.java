package com.fjx.mg.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.lxj.xpopup.core.BottomPopupView;


/**
 * 评论编辑pop
 */
public class BottomEditPopu extends BottomPopupView {

    private OnReplyListener onReplyListener;
    private EditText etComment;
    private Context mContext;


    public void setNickName(String nickName) {
        if (TextUtils.isEmpty(nickName)) {
            etComment.setHint(getContext().getString(R.string.reply));
        } else {
            etComment.setHint(getContext().getString(R.string.reply).concat(nickName));
        }
    }

    public BottomEditPopu(@NonNull Context context) {
        super(context);
        mContext=context;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        etComment = findViewById(R.id.etComment);
        GradientDrawableHelper.whit(etComment).setCornerRadius(50).setColor(R.color.colorGrayBg);
    }

    public void setOnReplyListener(OnReplyListener onReplyListener) {
        this.onReplyListener = onReplyListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_bottom_edit;
    }


    @Override
    protected void onShow() {
        super.onShow();

        findViewById(R.id.tvReply).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                String content = getContent();
                if (TextUtils.isEmpty(content)) {
                    CommonToast.toast(R.string.please_input_review);
                    return;
                }

                onReplyListener.onReply(content);
            }
        });
    }

    private String getContent() {
        return etComment.getText().toString();
    }

    public interface OnReplyListener {
        void onReply(String content);
    }
}
