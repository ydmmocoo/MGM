package com.fjx.mg.me.transfer;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.common.paylibrary.PayConfig;
import com.common.paylibrary.listener.PayCallback;
import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.common.paylibrary.model.WXPayModel;
import com.common.paylibrary.receiver.PayCallbackReceiver;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.friend.chat.ChatActivity;
import com.fjx.mg.pay.PayActivity;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.OtherUserModel;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatInfo;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatManager;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;
import com.tencent.qcloud.uikit.common.utils.TIMStringUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class MeTransferActivity extends BaseMvpActivity<MeTransferPresenter> implements MeTransferContact.View {
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPhone)
    TextView tvPhone;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.tvTranMoney)
    TextView tvTranMoney;


    private TIMFriend friend;
    private OtherUserModel userModel;

    private PayCallbackReceiver wxPayCallbackReceiver;

    public static Intent newInstance(Context context, String userInfo, String amount, String code) {
        Intent intent = new Intent(context, MeTransferActivity.class);
        intent.putExtra("userInfo", userInfo);
        intent.putExtra("amount", amount);
        intent.putExtra("code", code);
        return intent;
    }

    @Override
    protected MeTransferPresenter createPresenter() {
        return new MeTransferPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_im_transfer_money;
    }


    @Override
    protected void initView() {
        String userInfo = getIntent().getStringExtra("userInfo");
        String amount = getIntent().getStringExtra("amount");
        etMoney.setText(amount);
        if (amount != null && !amount.equals("")) {
            etMoney.setEnabled(false);
        }
        userModel = JsonUtil.strToModel(userInfo, OtherUserModel.class);
        friend = UserCenter.getFriend(userModel.getIdentifier());
        initShow();
        registerPayReceiver();
    }

    /**
     * 初始化显示
     */
    private void initShow() {
        ToolBarManager.with(this).setTitle(getString(R.string.transfer));
        GradientDrawableHelper.whit(tvTranMoney).setColor(R.color.colorAccent).setCornerRadius(50);
        if (userModel != null) {

            CommonImageLoader.load(userModel.getAvatar()).round().placeholder(R.drawable.default_user_image).into(ivAvatar);
        }
        if (friend != null) {
            CommonImageLoader.load(friend.getTimUserProfile().getFaceUrl()).round().placeholder(R.drawable.default_user_image).into(ivAvatar);
            tvName.setText(TextUtils.isEmpty(friend.getRemark()) ? friend.getTimUserProfile().getNickName() : friend.getRemark());
            String phone = TIMStringUtil.getPhone(friend.getTimUserProfile());
            tvPhone.setText(StringUtil.phoneText(phone));
        } else {
            tvName.setText(TextUtils.isEmpty(userModel.getUserName()) ? userModel.getUserNick() : userModel.getUserName());
            tvPhone.setText(StringUtil.phoneText(userModel.getPhone()));
        }

    }


    /**
     * 注册支付成功广播
     */
    private void registerPayReceiver() {
        wxPayCallbackReceiver = PayConfig.registPayReceiver(this, new PayCallback() {
            @Override
            public void payResult(PayExtModel extModel) {

            }
        });
    }

    @OnClick(R.id.tvTranMoney)
    public void onViewClicked() {
        //点击转账
        String mMoney = etMoney.getText().toString();
        String mRemark = etRemark.getText().toString();

        if (TextUtils.isEmpty(mMoney)) {
            CommonToast.toast(getString(R.string.hint_input_transfer));
            return;
        }

        float e = Float.parseFloat(mMoney);
        if (e < Constant.limitAmount) {
            CommonToast.toast(getString(R.string.limit_amount));
            return;
        }
        mPresenter.checkPrice(mMoney);

//        Map<String, Object> map = new HashMap<>();
//        map.put("receiverId", userModel.getUserId());
//        map.put("amount", mMoney);
//        map.put("instruction", mRemark);
//        map.put("type", "1");
//        String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.mg_transfer, map));
//        startActivityForResult(PayActivity.newInstance(getCurContext(), ext), 111);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) {

            setResult(resultCode);
            finish();
        }
    }

    private boolean isstart = false;

    @Override
    protected void onDestroy() {
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(this);
        boolean paystatus = sharedPreferencesHelper.getBoolean("paystatus");
        sharedPreferencesHelper.close();
        Log.e("!isstart" + !isstart, "!paystatus" + !paystatus);
        if (!isstart || !paystatus) {
            if (getIntent().getStringExtra("code") != null) {
                if (!getIntent().getStringExtra("code").equals("")) {
                    Intent intent = new Intent(PayActivity.broadcast_codepay);
                    intent.putExtra("code", getIntent().getStringExtra("code"));
                    intent.putExtra("amount", "");
                    intent.putExtra("status", "3");
                    sendBroadcast(intent);
                }
            }
        }
        if (wxPayCallbackReceiver != null)
            unregisterReceiver(wxPayCallbackReceiver);
        super.onDestroy();

    }

    @Override
    public void checkSuccess() {
        if (getIntent().getStringExtra("code") != null) {
            if (!getIntent().getStringExtra("code").equals("")) {
                SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
                sp.putBoolean("paystatus", false);
                sp.close();
            }
        }
        //点击转账
        String mMoney = etMoney.getText().toString();
        String mRemark = etRemark.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("receiverId", userModel.getUserId());
        map.put("amount", mMoney);
        map.put("instruction", mRemark);
        map.put("type", "1");
        map.put("payCode", getIntent().getStringExtra("code"));
        String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.mg_transfer, map));
        isstart = true;
        startActivityForResult(PayActivity.newInstance(getCurContext(), ext), 111);

    }
}
