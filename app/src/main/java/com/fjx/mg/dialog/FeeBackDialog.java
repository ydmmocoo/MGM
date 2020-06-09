package com.fjx.mg.dialog;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.lxj.xpopup.core.AttachPopupView;

public class FeeBackDialog extends AttachPopupView implements View.OnClickListener {
    private MenuDeleteDialogClickListener cl;

    public FeeBackDialog(@NonNull Activity context, MenuDeleteDialogClickListener cl) {
        super(context);
        this.cl = cl;
    }

    public interface MenuDeleteDialogClickListener {
        void onClick(View view);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_fee_back;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.tvQrScan).setOnClickListener(this);

        findViewById(R.id.tvQrCode).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        cl.onClick(v);
    }
}
