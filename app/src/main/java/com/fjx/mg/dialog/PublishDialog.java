package com.fjx.mg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;

public class PublishDialog extends AttachPopupView {

    private TextView tvClose;
    private TextView tvEdit;
    private TextView tvDelete;

    private TextView tvLove;
    private TextView tvShare;
    private OnSelectListener onSelectListener;
    private boolean isExpire;
    private boolean isLove;
    private boolean isShare;
    private boolean hasClosed;
    private boolean isCompany;

    public PublishDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_publish_setting;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvClose = findViewById(R.id.tvClose);
        tvEdit = findViewById(R.id.tvEdit);
        tvDelete = findViewById(R.id.tvDelete);

        tvLove = findViewById(R.id.tvLove);
        tvShare = findViewById(R.id.tvShare);

        tvLove.setVisibility(isLove ? GONE : VISIBLE);

        tvClose.setVisibility(isExpire ? GONE : VISIBLE);
        tvEdit.setVisibility(isExpire ? GONE : VISIBLE);
        tvClose.setText(hasClosed ? getContext().getString(R.string.open) : getContext().getText(R.string.closed));
        tvDelete.setVisibility(isExpire ? GONE: VISIBLE);

//        tvClose.setVisibility(isCompany ? GONE : VISIBLE);

        tvClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSelectListener.onSelect(0, "");
            }
        });
        tvEdit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSelectListener.onSelect(1, "");
            }
        });
        tvDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSelectListener.onSelect(2, "");
            }
        });
        tvLove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSelectListener.onSelect(3, "");
            }
        });
        tvShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onSelectListener.onSelect(4, "");
            }
        });
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public void isExpire(boolean isExpire, boolean hasClosed, boolean isLove, boolean isShare) {
        this.isExpire = isExpire;
        this.hasClosed = hasClosed;
        this.isLove = isLove;
        this.isShare = isShare;
    }

    public void isExpire(boolean isLove) {
        this.isLove = isLove;
    }

    public void setCompany() {
        this.isCompany = true;
    }


}
