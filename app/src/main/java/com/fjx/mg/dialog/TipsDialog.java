package com.fjx.mg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fjx.mg.R;

/**
 * Author    by hanlz
 * Date      on 2019/12/12.
 * Description：
 */
public class TipsDialog extends Dialog {

    private TextView mTvTitle;
    private String mTitle;

    public TipsDialog(@NonNull Context context, String title) {
        super(context);
        mTitle = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tips);
        mTvTitle = findViewById(R.id.tvTitle);
        findViewById(R.id.tvSure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        if (!TextUtils.isEmpty(mTitle))
            mTvTitle.setText(mTitle);
    }

}
