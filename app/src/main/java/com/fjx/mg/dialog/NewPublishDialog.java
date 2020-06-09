package com.fjx.mg.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.lxj.xpopup.core.AttachPopupView;
import com.lxj.xpopup.interfaces.OnSelectListener;

/**
 * @author yedeman
 * @date 2020/5/13.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class NewPublishDialog extends AttachPopupView {

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

    public NewPublishDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_new_publish;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvClose = findViewById(R.id.tvClose);
        tvEdit = findViewById(R.id.tvEdit);
        tvDelete = findViewById(R.id.tvDelete);

        tvLove = findViewById(R.id.tvLove);
        tvShare = findViewById(R.id.tvShare);

        tvClose.setOnClickListener(v -> {
            dismiss();
            onSelectListener.onSelect(0, "");
        });
        tvEdit.setOnClickListener(v -> {
            dismiss();
            onSelectListener.onSelect(1, "");
        });
        tvDelete.setOnClickListener(v -> {
            dismiss();
            onSelectListener.onSelect(2, "");
        });
        tvLove.setOnClickListener(v -> {
            dismiss();
            onSelectListener.onSelect(3, "");
        });
        tvShare.setOnClickListener(v -> {
            dismiss();
            onSelectListener.onSelect(4, "");
        });
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public void isExpire(boolean isLove) {
        this.isLove = isLove;
    }

    public void setCompany() {
        this.isCompany = true;
    }
}
