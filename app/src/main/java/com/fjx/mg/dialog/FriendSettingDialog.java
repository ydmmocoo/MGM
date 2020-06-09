package com.fjx.mg.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.fjx.mg.friend.addfriend.SearchFriendActivity;
import com.fjx.mg.friend.contacts.FriendContactsActivity;
import com.fjx.mg.friend.nearby.NearbyActivity;
import com.fjx.mg.main.qrcode.CaptureActivity;
import com.fjx.mg.main.scan.QRCodeCollectionActivity;
import com.library.common.utils.GradientDrawableHelper;
import com.lxj.xpopup.core.AttachPopupView;

import pub.devrel.easypermissions.EasyPermissions;

import static com.mob.tools.utils.Strings.getString;

public class FriendSettingDialog extends AttachPopupView {

    private TextView tvContact;
    private TextView viewDot;
    private Activity context;
    private int REQUESTCODE_CAMERA = 2;

    public FriendSettingDialog(@NonNull Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_friend_setting;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvContact = findViewById(R.id.tvContact);

        findViewById(R.id.tvAddFriend).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(SearchFriendActivity.newInstance(v.getContext()));
                dismiss();
            }
        });

        tvContact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(FriendContactsActivity.newInstance(v.getContext(), false));
                dismiss();
            }
        });
        findViewById(R.id.nearAddFriend).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(NearbyActivity.newInstance(context));
                dismiss();
            }
        });
        findViewById(R.id.tvQrScan).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EasyPermissions.hasPermissions(context, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    context.startActivityForResult(new Intent(context, CaptureActivity.class), 1);
                } else {
                    EasyPermissions.requestPermissions(context, getString(R.string.permission_camata_sd),
                            1, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                dismiss();
            }
        });
        findViewById(R.id.tvQrCode).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(QRCodeCollectionActivity.newInstance(v.getContext()));
                dismiss();
            }
        });
    }

    public void showDot(String num) {
        if (viewDot == null) {
            viewDot = findViewById(R.id.viewDot);
            GradientDrawableHelper.whit(viewDot).setColor(R.color.colorAccent).setCornerRadius(20);
        }
        boolean isShow = !TextUtils.isEmpty(num) && !TextUtils.equals(num, "0");
        viewDot.setVisibility(isShow ? VISIBLE : GONE);
        viewDot.setText(num);
    }
}
