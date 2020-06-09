package com.fjx.mg.friend.chat.redpacket;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.paylibrary.PayConfig;
import com.common.paylibrary.listener.PayCallback;
import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.common.paylibrary.receiver.PayCallbackReceiver;
import com.fjx.mg.R;
import com.fjx.mg.pay.PayActivity;
import com.library.common.base.BaseActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.core.net.CommonObserver;
import com.library.repository.core.net.RxScheduler;
import com.library.repository.models.ResponseModel;
import com.library.repository.repository.RepositoryFactory;
import com.library.repository.util.LogUtil;
import com.tencent.qcloud.uikit.business.chat.model.MessageInfoUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author    by hanlz
 * Date      on 2019/12/6.
 * Description：群红包
 */
public class GroupRedPacketActivity extends BaseActivity {

    @BindView(R.id.ivBack)
    ImageView mIvBack;
    @BindView(R.id.tvSendRedPacket)
    TextView mTvSendRedPacket;//发红包
    @BindView(R.id.tvLuckyRedPacket)
    TextView mTvLuckyRedPacket;
    @BindView(R.id.tvCommonRedPacket)
    TextView mTvCommonRedPacket;
    @BindView(R.id.etInputAmount)
    EditText mEtInputAmount;
    @BindView(R.id.etInputUnit)
    EditText mEtInputUnit;
    @BindView(R.id.etSay)
    EditText mEtSay;
    @BindView(R.id.tvAmountTips)
    TextView mTvAmountTips;

    private String type = "1";//1:拼手气红包，2：普通红包
    private PayCallbackReceiver payCallbackReceiver;

    public static Intent newIntent(Context context, String ext, String groupId) {
        Intent intent = new Intent(context, GroupRedPacketActivity.class);
        intent.putExtra(IntentConstants.GROUP_ID, groupId);
        intent.putExtra(IntentConstants.EXT, ext);
        return intent;
    }


    @Override
    protected int layoutId() {
        return R.layout.act_group_red_packet;
    }

    @Override
    public void initView() {
        GradientDrawableHelper.whit(mTvSendRedPacket).setColor(R.color.colorAccent).setCornerRadius(50);
        registerPayReceiver();
    }


    @OnClick({R.id.tvLuckyRedPacket, R.id.tvCommonRedPacket, R.id.tvSendRedPacket, R.id.ivBack})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;
            case R.id.tvSendRedPacket://发红包
                String amount = mEtInputAmount.getText().toString();

                if (TextUtils.isEmpty(amount)) {
                    CommonToast.toast(getString(R.string.hint_input_redpacket));
                    return;
                }

                float e = Float.parseFloat(amount);
                if (e < Constant.limitAmount) {
                    CommonToast.toast(getString(R.string.limit_amount));
                    return;
                }
                String unit = mEtInputUnit.getText().toString();
                if (TextUtils.isEmpty(unit)) {
                    CommonToast.toast(getString(R.string.please_input_num));
                    return;
                }

                checkLimit(amount, unit);
                break;
            case R.id.tvLuckyRedPacket://手气红包
                setRedPacketType(true);
                break;
            case R.id.tvCommonRedPacket://普通红包
                setRedPacketType(false);
                break;
            default:
                break;
        }
    }

    /**
     * @param type true---手气红包  false---普通红包
     */
    private void setRedPacketType(boolean type) {
        if (type) {
            mTvLuckyRedPacket.setBackgroundResource(R.drawable.send_group_red_packet_text_select);
            mTvLuckyRedPacket.setTextColor(getResources().getColor(R.color.c_d02934));
            mTvCommonRedPacket.setBackgroundResource(R.drawable.send_group_red_packet_text_unselect);
            mTvCommonRedPacket.setTextColor(getResources().getColor(android.R.color.white));
            mTvAmountTips.setText(getString(R.string.total_amount));
            this.type = "1";
        } else {
            mTvLuckyRedPacket.setBackgroundResource(R.drawable.send_group_red_packet_text_unselect);
            mTvLuckyRedPacket.setTextColor(getResources().getColor(android.R.color.white));
            mTvCommonRedPacket.setBackgroundResource(R.drawable.send_group_red_packet_text_select);
            mTvCommonRedPacket.setTextColor(getResources().getColor(R.color.c_d02934));
            mTvAmountTips.setText(getString(R.string.one_amount));
            this.type = "2";
        }
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
        String remark = "";
        if (StringUtil.isEmpty(mEtSay.getText().toString())) {
            remark = mEtSay.getHint().toString();
        } else {
            remark = mEtSay.getText().toString();
        }
        String json = MessageInfoUtil.createGroupRedPacketJson(mEtInputAmount.getText().toString(), remark, message, MessageInfoUtil.TRANSFER_RED_PACKET_UN_RECEIVED);
        Intent intent = new Intent();
        intent.putExtra("message", json);
        setResult(1, intent);
        finish();

    }

    public void checkLimit(final String price, final String num) {
        RepositoryFactory.getRemotePayRepository().checkMoneyLimit2("3"/*群红包*/, price, type, num)
                .compose(RxScheduler.<ResponseModel<Object>>toMain())
                .as(this.<ResponseModel<Object>>bindAutoDispose())
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        Map<String, Object> map = new HashMap<>();
                        map.put(IntentConstants.GROUP_ID, getIntent().getStringExtra(IntentConstants.GROUP_ID));
                        if ("2".equals(type)) {
                            String multiply = StringUtil.multiply(price, num);
                            map.put("amount", multiply);
                        } else {
                            map.put("amount", mEtInputAmount.getText().toString());
                        }
                        map.put("num", mEtInputUnit.getText().toString());
                        map.put("type", type);
                        if (StringUtil.isEmpty(mEtSay.getText().toString())) {
                            map.put("remark", mEtSay.getHint().toString());
                        } else {
                            map.put("remark", mEtSay.getText().toString());
                        }
                        if (mEtSay.getText().toString().getBytes().length >= 75) {
                            CommonToast.toast(R.string.remark_more);
                            return;
                        }
                        String ext = JsonUtil.moderToString(new PayExtModel(UsagePayMode.im_group_red_packet, map));
                        startActivity(PayActivity.newInstance(getCurContext(), ext));
                    }

                    @Override
                    public void onError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }

                    @Override
                    public void onNetError(ResponseModel data) {
                        CommonToast.toast(data.getMsg());
                    }
                });
    }


}
