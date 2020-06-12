package com.fjx.mg.me.invite;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.ShareDialog;
import com.fjx.mg.dialog.ShareDialog2;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.library.common.base.BaseMvpActivity;
import com.library.common.callback.OnUiCallback;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.IntentUtil;
import com.library.common.utils.RxJavaUtls;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.InviteModel;
import com.library.repository.models.RechargeModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;

public class InviteActivity extends BaseMvpActivity<InvitePresenter> implements InviteContract.View {

    @BindView(R.id.tvInviteCode)
    TextView tvInviteCode;
    @BindView(R.id.tvCopy)
    TextView tvCopy;
    @BindView(R.id.ivQrCode)
    ImageView ivQrCode;
    @BindView(R.id.ivInvite)
    ImageView tvInvite;
    private String shareContent;
    private String inviteDesc;
    private String shareTitle;


    @Override
    protected InvitePresenter createPresenter() {
        return new InvitePresenter(this);
    }

    public static Intent newInstance(Context context) {
        Intent intent = new Intent(context, InviteActivity.class);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.ac_invite;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.invite_friend));
        GradientDrawableHelper.whit(tvCopy).setColor(R.color.white).setStroke(1, R.color.text_color_gray).setCornerRadius(0);
        try {
            SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
            Gson gson = new Gson();
            String json = sp.getString("InviteModel");
            if (!json.equals("")) {
                Log.e("json:", "" + json);
                InviteModel statusLs = gson.fromJson(json, new TypeToken<InviteModel>() {
                }.getType());

                showInviteMessage(statusLs);
                mPresenter.getBitmap(statusLs.getRegisterUrl());
            }
            sp.close();

        } catch (Exception e) {
            Log.e("Exception:", "" + e.toString());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getInviteCode(this);
    }

    @OnClick({R.id.tvCopy, R.id.ivInvite})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCopy:
                String text = tvInviteCode.getText().toString();
                StringUtil.copyClip(text);
                CommonToast.toast(getString(R.string.invite_code_copy_success));
                break;
            case R.id.ivInvite:
                showShareDialog();
                break;
        }
    }

    @Override
    public void showQrBitmap(Bitmap bitmap) {
        ivQrCode.setImageBitmap(bitmap);
    }

    @Override
    public void showInviteMessage(InviteModel model) {
        tvInviteCode.setText(model.getInviteCode());
        shareContent = model.getSmsTemplate();
        inviteDesc = model.getInviteDesc();
        shareTitle = model.getInviteTitle();
    }


    @Override
    public void showSelectPhoneNUm(String number) {
        if (TextUtils.isEmpty(shareContent)) return;
        IntentUtil.sendSmsMessage(number, shareContent);
    }

    private void showShareDialog() {
        String imageUrl = "";
        String webview = Constant.INVITE_URL.concat(tvInviteCode.getText().toString()).concat("&l=").concat(RepositoryFactory.getLocalRepository().getLangugeType());

        ShareDialog2 shareDialog = new ShareDialog2(getCurContext());
        shareDialog.setShareParams(shareTitle, inviteDesc, imageUrl, webview);
        shareDialog.setShareType(ShareDialog2.ShareType.web);
        new XPopup.Builder(getCurContext())
                .asCustom(shareDialog)
                .show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (resultCode == RESULT_OK && requestCode == 100) {
            mPresenter.getPhoneNum(data);
        }
    }
}
