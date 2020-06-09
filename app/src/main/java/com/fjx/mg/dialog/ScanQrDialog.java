package com.fjx.mg.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.fjx.mg.main.qrcode.CaptureActivity;
import com.fjx.mg.main.scan.QRCodeCollectionActivity;
import com.lxj.xpopup.core.AttachPopupView;

import pub.devrel.easypermissions.EasyPermissions;

import static com.mob.tools.utils.Strings.getString;

public class ScanQrDialog extends AttachPopupView {

    private Activity context;
    private int REQUESTCODE_CAMERA = 2;

    public ScanQrDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_scan_qr;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        findViewById(R.id.tvQrScan).setOnClickListener(v -> {
            if (EasyPermissions.hasPermissions(context, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                context.startActivityForResult(new Intent(context, CaptureActivity.class), 1);
            } else {
                EasyPermissions.requestPermissions(context, getString(R.string.permission_camata_sd),
                        1, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            dismiss();
        });

        findViewById(R.id.tvQrCode).setOnClickListener(v -> {
            v.getContext().startActivity(QRCodeCollectionActivity.newInstance(v.getContext()));
            dismiss();
        });
    }
}
