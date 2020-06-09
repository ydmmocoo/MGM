package com.tencent.qcloud.uikit.common.widget;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.library.common.utils.CommonToast;
import com.tencent.qcloud.uikit.R;

/**
 * Author    by hanlz
 * Date      on 2019/12/4.
 * Description：
 */
public class GroupNicknameModificationDialog extends DialogFragment {


    private EditText mEtContent;
    private TextView mTvTitle;
    private TextView mTvSure;
    private TextView mTvCancel;


    private String hint;
    private onModificationListener monModificationLIstener;


    @Override
    public void onStart() {
        super.onStart();
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window win = getDialog().getWindow();
        // 一定要设置Background，如果不设置，window属性设置无效
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        WindowManager.LayoutParams params = win.getAttributes();
        params.gravity = Gravity.BOTTOM;
        // 使用ViewGroup.LayoutParams，以便Dialog 宽度充满整个屏幕
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        win.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_modification_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mEtContent = view.findViewById(R.id.etContent);
        mTvTitle = view.findViewById(R.id.tvTitle);
        mTvSure = view.findViewById(R.id.tvSure);
        mTvCancel = view.findViewById(R.id.tvCancel);

        mTvTitle.setText(getString(R.string.modification_group_nickname));
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mTvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mEtContent.getText().toString())) {
                    if (monModificationLIstener != null) {
                        monModificationLIstener.onSure(mEtContent.getText().toString());
                    }
                    dismiss();
                } else {
                    if (!TextUtils.isEmpty(hint)) {
                        CommonToast.toast(hint);
                    } else {
                        CommonToast.toast(R.string.please_modification_group_nickname);
                    }
                    return;
                }

            }
        });
    }


    public void setOnMonModificationListener(onModificationListener listener) {
        this.monModificationLIstener = listener;
    }

    public interface onModificationListener {
        void onSure(String name);
    }
}
