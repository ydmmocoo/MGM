package com.fjx.mg.me.transfer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

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

public class MeTransferActivityx extends BaseMvpActivity<MeTransferPresenterx> implements MeTransferContactx.View {
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

    public static Intent newInstance(Context context, String userInfo) {
        Intent intent = new Intent(context, MeTransferActivityx.class);
        intent.putExtra("userInfo", userInfo);
        return intent;
    }

    @Override
    protected MeTransferPresenterx createPresenter() {
        return new MeTransferPresenterx(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_im_transfer_money;
    }


    @Override
    protected void initView() {
        String userInfo = getIntent().getStringExtra("userInfo");
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
//        mPresenter.checkPrice(mMoney);
        mPresenter.checkSameMoney(userModel.getUserId(), mMoney);
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

    @Override
    protected void onDestroy() {
        if (wxPayCallbackReceiver != null)
            unregisterReceiver(wxPayCallbackReceiver);
        super.onDestroy();
    }

    @Override
    public void checkSuccess() {
        //点击转账
        String mMoney = etMoney.getText().toString();
        String mRemark = etRemark.getText().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("receiverId", userModel.getUserId());
        map.put("amount", mMoney);
        map.put("instruction", mRemark);
        map.put("type", "1");
        map.put("outOrderId", userModel.getUserId() + System.currentTimeMillis());
        String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.mg_transfer, map));
        startActivityForResult(PayActivity.newInstance(getCurContext(), ext), 111);
    }

    @Override
    public void sameMoney(boolean isSame, String msg) {
        //点击转账
        final String mMoney = etMoney.getText().toString();
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
        if (!isSame) {
            //不是同一个人或者第一次转账
            mPresenter.checkPrice(mMoney);
        } else {
            AlertDialog dialog = new AlertDialog.Builder(getCurContext()).setTitle(getString(R.string.tips))
                    .setMessage(msg)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            mPresenter.checkPrice(mMoney);
                        }
                    })
                    .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create();
            if (!dialog.isShowing() && !isFinishing()) {
                dialog.show();
            }
        }
    }
}
