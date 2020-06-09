package com.fjx.mg.dialog;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.lxj.xpopup.core.AttachPopupView;

public class ScanQrDialogx extends AttachPopupView implements View.OnClickListener {
    private Activity context;
    private int REQUESTCODE_CAMERA = 2;
    private MenuDeleteDialogClickListener cl;

    public ScanQrDialogx(@NonNull Activity context, MenuDeleteDialogClickListener cl) {
        super(context);
        this.cl = cl;
        this.context = context;
    }

    public interface MenuDeleteDialogClickListener {
        void onClick(View view);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_scan_qr;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.tvQrScan).setOnClickListener(this);

        findViewById(R.id.tvQrCode).setOnClickListener(this);

        findViewById(R.id.tvBarCode).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        cl.onClick(v);
    }
}
