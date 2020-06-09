package com.fjx.mg.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.fjx.mg.R;
import com.library.common.utils.GradientDrawableHelper;
import com.lxj.xpopup.core.AttachPopupView;

public class FriendSettingDialogx extends AttachPopupView implements View.OnClickListener {

    private TextView tvContact;
    private TextView viewDot;
    private Activity context;
    private int REQUESTCODE_CAMERA = 2;
    private FriendSettingDialogxClickListener cl;

    public FriendSettingDialogx(@NonNull Activity context, FriendSettingDialogxClickListener cl) {
        super(context);
        this.context = context;
        this.cl = cl;
    }

    @Override
    public void onClick(View v) {
        cl.onClick(v);
    }

    public interface FriendSettingDialogxClickListener {
         void onClick(View view);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_friend_setting;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        tvContact = findViewById(R.id.tvContact);

        findViewById(R.id.tvAddFriend).setOnClickListener(this);

        tvContact.setOnClickListener(this);
        findViewById(R.id.tvCreateGroupChat).setOnClickListener(this);
        findViewById(R.id.nearAddFriend).setOnClickListener(this);
        findViewById(R.id.tvQrScan).setOnClickListener(this);
        findViewById(R.id.tvQrCode).setOnClickListener(this);
        findViewById(R.id.tvBarCode).setOnClickListener(this);
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
