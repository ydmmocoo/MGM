package com.fjx.mg.friend.redpacker;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.common.paylibrary.PayConfig;
import com.common.paylibrary.listener.PayCallback;
import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.common.paylibrary.receiver.PayCallbackReceiver;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.pay.PayActivity;
import com.fjx.mg.view.PriceTextWatcher;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.qcloud.uikit.business.chat.c2c.model.C2CChatManager;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfo;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RedPacketActivity extends BaseMvpActivity<RedPacketPresenter> implements RedPacketContact.View {

    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.tvTranMoney)
    TextView tvTranMoney;
    private MessageInfo mMessageInfo;
    private String imUid;
    private String mMoney;
    private String mRemark;
    private PayCallbackReceiver payCallbackReceiver;

    public static Intent newInstance(Context context, String message, String imUid) {
        Intent intent = new Intent(context, RedPacketActivity.class);
        intent.putExtra("MessageInfo", message);
        intent.putExtra("imUid", imUid);
        return intent;
    }

    @Override
    protected RedPacketPresenter createPresenter() {
        return new RedPacketPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_im_red_packet;
    }


    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.red_pack));
        GradientDrawableHelper.whit(tvTranMoney).setColor(R.color.colorAccent).setCornerRadius(50);
        registerPayReceiver();
        imUid = getIntent().getStringExtra("imUid");
        TIMFriend friend = UserCenter.getFriend(imUid);
//        if (friend == null) {
//            CommonToast.toast(getString(R.string.not_your_friend));
//            finish();
//            return;
//        }
        etMoney.addTextChangedListener(new PriceTextWatcher());
        String message = getIntent().getStringExtra("MessageInfo");
        Log.d("MessageInfo", message);
        if (!TextUtils.isEmpty(message)) {
            mMessageInfo = JsonUtil.strToModel(message, MessageInfo.class);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SoftInputUtil.showSoftInputView(getCurActivity(), etMoney);
            }
        }, 100);

    }

    @OnClick(R.id.tvTranMoney)
    public void onViewClicked() {
        SoftInputUtil.hideSoftInput(this);
        mMoney = etMoney.getText().toString();
        mRemark = etRemark.getText().toString();


        if (TextUtils.isEmpty(mMoney)) {
            CommonToast.toast(getString(R.string.hint_input_redpacket));
            return;
        }

        float e = Float.parseFloat(mMoney);
        if (e < Constant.limitAmount) {
            CommonToast.toast(getString(R.string.limit_amount));
            return;
        }

        mPresenter.checkPrice(mMoney);
    }

    /**
     * 注册支付成功广播
     */
    private void registerPayReceiver() {
        payCallbackReceiver = PayConfig.registPayReceiver(this, new PayCallback() {
            @Override
            public void payResult(PayExtModel extModel) {
                sendMessage((String) extModel.getMessage());

            }
        });
    }

    private void sendMessage(String message) {

        String json = MessageInfoUtil.createJson(mMoney, mRemark, message, MessageInfoUtil.TRANSFER_RED_PACKET_UN_RECEIVED);
        Intent intent = new Intent();
        intent.putExtra("message", json);
        setResult(1, intent);
        finish();

    }

    @Override
    protected void onDestroy() {
        if (payCallbackReceiver != null)
            unregisterReceiver(payCallbackReceiver);
        super.onDestroy();
    }

    @Override
    public void checkSuccess() {
        mPresenter.checkPrice(mMoney);
        Map<String, Object> map = new HashMap<>();
        map.put("receiverId", imUid);
        map.put("amount", mMoney);
        map.put("instruction", mRemark);
        map.put("type", "2");
        String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.im_redpacket, map));
        startActivity(PayActivity.newInstance(getCurContext(), ext));
    }
}
