package com.fjx.mg.friend.transfer;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
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
import com.fjx.mg.view.PriceTextWatcher;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMUserProfile;
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

public class TransferMoneyActivity extends BaseMvpActivity<TransferMoneyPresenter> implements TransferMoneyContact.View {
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

    private MessageInfo mMessageInfo;
    private String imUid;

    private boolean goChat;
    private TIMFriend friend;
    private TIMUserProfile profile;

    private PayCallbackReceiver wxPayCallbackReceiver;
    private String mMoney, mRemark;

    public static Intent newInstance(Context context, String message, String imUid) {
        Intent intent = new Intent(context, TransferMoneyActivity.class);
        intent.putExtra("MessageInfo", message);
        intent.putExtra("imUid", imUid);
        return intent;
    }

    @Override
    protected TransferMoneyPresenter createPresenter() {
        return new TransferMoneyPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_im_transfer_money;
    }


    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.transfer));
        imUid = getIntent().getStringExtra("imUid");
        mPresenter.findUser(imUid);

        registerPayReceiver();

        etMoney.addTextChangedListener(new PriceTextWatcher());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SoftInputUtil.showSoftInputView(getCurActivity(), etMoney);
            }
        }, 100);
    }

    /**
     * 初始化显示
     */
    private void initShow() {
        GradientDrawableHelper.whit(tvTranMoney).setColor(R.color.colorAccent).setCornerRadius(50);
        CommonImageLoader.load(getFaceUrl()).round().placeholder(R.drawable.default_user_image).into(ivAvatar);
        tvName.setText(getRemark());

        initMessageInfo();
    }

    /**
     * 初始化消息体
     */
    private void initMessageInfo() {
        String message = getIntent().getStringExtra("MessageInfo");
        Log.d("MessageInfo", message);
        if (!TextUtils.isEmpty(message)) {
            mMessageInfo = JsonUtil.strToModel(message, MessageInfo.class);
        } else {
            goChat = true;
            //这边不是从聊天界面过来的，而是从好友列表过来的，发送转账消息前需要先生成一个会话
            mMessageInfo = MessageInfoUtil.buildTransferCustomMessage(MessageInfoUtil.TRANSFER_ACCOUNT_NEW_RECEIVED, "");
            createSessionInfo();
        }
    }


    /**
     * 注册支付成功广播
     */
    private void registerPayReceiver() {
        wxPayCallbackReceiver = PayConfig.registPayReceiver(this, new PayCallback() {
            @Override
            public void payResult(PayExtModel extModel) {
//                CommonToast.toast(JsonUtil.moderToString(extModel));
                sendMessage((String) extModel.getMessage());
            }
        });
    }

    @OnClick(R.id.tvTranMoney)
    public void onViewClicked() {
        //点击转账
        mMoney = etMoney.getText().toString();
        mRemark = etRemark.getText().toString();
        if (TextUtils.isEmpty(mMoney)) {
            CommonToast.toast(getString(R.string.hint_input_amount));
            return;
        }

//        if (TextUtils.isEmpty(mMoney)) {
//            CommonToast.toast(getString(R.string.hint_input_transfer));
//            return;
//        }

        float e = Float.parseFloat(mMoney);
        if (e < Constant.limitAmount) {
            CommonToast.toast(getString(R.string.limit_amount));
            return;
        }

        mPresenter.checkPrice(mMoney);
//        Map<String, Object> map = new HashMap<>();
//        map.put("receiverId", imUid);
//        map.put("amount", mMoney);
//        map.put("instruction", mRemark);
//        map.put("type", "1");
//        String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.im_transfer, map));
//        startActivity(PayActivity.newInstance(getCurContext(), ext));
//        mPresenter.transMoneyByWX(imUid, mMoney, mRemark);
    }


    /**
     * 发送消息
     */
    private void sendMessage(String transferId) {
        if (TextUtils.isEmpty(mRemark)) mRemark = getString(R.string.transfer_explain);
        String json = MessageInfoUtil.createJson(mMoney, mRemark, transferId, MessageInfoUtil.TRANSFER_ACCOUNT_NEW_RECEIVED);
        if (goChat) {
            String remark = getRemark();
            ChatActivity.startC2CChat(this, imUid, remark, json);
        } else {
            Intent intent = new Intent();
            intent.putExtra("message", json);
            setResult(1, intent);
        }
        finish();
    }

    private void createSessionInfo() {
        C2CChatInfo chatInfo = new C2CChatInfo();
        chatInfo.setPeer(imUid);
        chatInfo.setChatName(getRemark());
        C2CChatManager.getInstance().addChatInfo(chatInfo);
        C2CChatManager.getInstance().setCurrentChatInfo(chatInfo);
    }

    @Override
    public void getWXOrderSuccess(WXPayModel payModel) {
        mPresenter.payWX(payModel);

    }

    @Override
    public void showUserInfo(TIMUserProfile userProfile) {
        this.profile = userProfile;
        initShow();
        String phone = TIMStringUtil.getPhone(userProfile);
        tvPhone.setText(StringUtil.phoneText(phone));
    }

    @Override
    public void showUserInfo(TIMFriend friend) {
        this.friend = friend;
        String phone = TIMStringUtil.getPhone(friend.getTimUserProfile());
        tvPhone.setText(StringUtil.phoneText(phone));
        initShow();
    }

    @Override
    public void checkSuccess() {
        //点击转账
        mMoney = etMoney.getText().toString();
        mRemark = etRemark.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("receiverId", imUid.replace("fjx","")
                .replace("MGM",""));
        map.put("amount", mMoney);
        map.put("instruction", mRemark);
        map.put("type", "1");
        map.put("outOrderId", imUid.replace("fjx","").replace("MGM","") + System.currentTimeMillis());
        //String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.im_transfer, map));
        String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.mg_transfer, map));
        //startActivity(PayActivity.newInstance(getCurContext(), ext));
        startActivityForResult(PayActivity.newInstance(getCurContext(), ext), 111);
    }

    @Override
    protected void onDestroy() {
        if (wxPayCallbackReceiver != null)
            unregisterReceiver(wxPayCallbackReceiver);
        super.onDestroy();
    }

    private String getRemark() {
        if (friend == null) {
            return profile.getNickName();
        } else {
            return TextUtils.isEmpty(friend.getRemark()) ? friend.getTimUserProfile().getNickName() : friend.getRemark();
        }
    }

    private String getFaceUrl() {
        if (friend == null) {
            return profile.getFaceUrl();
        } else {
            return friend.getTimUserProfile().getFaceUrl();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) {
            setResult(resultCode);
            finish();
        }
    }
}
