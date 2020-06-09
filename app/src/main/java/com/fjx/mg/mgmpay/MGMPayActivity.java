package com.fjx.mg.mgmpay;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.login.login.LoginActivity;
import com.fjx.mg.me.wallet.recharge.BalanceRechargeActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StringUtil;
import com.library.common.view.editdialog.PayFragment;
import com.library.common.view.editdialog.PayPwdView;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.PayByBalanceModel;
import com.library.repository.models.PayCheckOrderModel;
import com.library.repository.models.ResponseModel;
import com.library.repository.models.UserInfoModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author    by hanlz
 * Date      on 2020/3/3.
 * Description：
 */
public class MGMPayActivity extends BaseMvpActivity<MGMpayPresenter> implements MGMPayContact.View {

    private String mOrderString;
    private float mPrice;

    @BindView(R.id.cbBalancePay)
    CheckBox mCbBalancePay;
    @BindView(R.id.tvBalance)
    TextView mTvBalance;
    @BindView(R.id.tvARPrice)
    TextView mTvARPrice;
    @BindView(R.id.tvPayeeName)
    TextView mTvPayeeName;
    @BindView(R.id.tvOrderInfo)
    TextView mTvOrderInfo;
    private int mPayType = Constant.PayType.ACCOUNT;
    Map<String, String> map = new HashMap<>();

    @Override
    protected MGMpayPresenter createPresenter() {
        return new MGMpayPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.act_mgm_pay;
    }

    @Override
    protected void initView() {
        super.initView();
        ToolBarManager.with(this).setTitle(getString(R.string.payment)).setNavigationIcon(R.drawable.ic_back_black);
        if (getIntent() != null) {
            mOrderString = getIntent().getStringExtra(IntentConstants.ORDER_STRING);
            mPrice = getIntent().getFloatExtra(IntentConstants.PRICE, 0.0f);
            mTvARPrice.setText(String.valueOf(mPrice).concat(getString(R.string.ar)));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getUserProfile();
    }

    @OnClick(R.id.tvBuyNow)
    public void onBuyNow(View view) {
        if (mPayType == Constant.PayType.ACCOUNT) {
            if (mPrice > Float.valueOf(UserCenter.getUserInfo().getUMoney())) {
                startActivityForResult(BalanceRechargeActivity.newInstance(getCurContext()), 111);
                CommonToast.toast(getString(R.string.Insufficient_Balance));
                return;
            }
            mPresenter.selectPswDialog(map);
        }
    }


    /**
     * 选择支付类型 目前只有余额支付 后面可以会和支付宝一样对接当地银行
     */
    @OnClick(R.id.llBalancePay)
    public void onPay(View view) {
        reset();
        switch (view.getId()) {
            case R.id.llBalancePay://余额支付
                mCbBalancePay.setChecked(true);
                mPayType = Constant.PayType.ACCOUNT;
                break;
            default:
                break;

        }
    }

    @OnClick(R.id.tvGoRecharge)
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tvGoRecharge:
                startActivityForResult(BalanceRechargeActivity.newInstance(getCurContext()), 111);
                break;
        }
    }

    private void result(String code, String msg) {
        Intent intent = new Intent("com.fjx.pay_result");
        intent.putExtra(IntentConstants.CODE, code);
        intent.putExtra(IntentConstants.RESULT, msg);
        sendBroadcast(intent);
        finish();
    }


    private void reset() {
        mCbBalancePay.setChecked(false);
    }

    @Override
    public void responsePayByBalanceFail(ResponseModel data) {
        result(String.valueOf(data.getCode()), data.getMsg());
    }

    @Override
    public void responsePayByBalanceSuc(PayByBalanceModel model) {
        startActivity(MGMPayResultActivity.newIntent(this, mTvARPrice.getText().toString().trim()));
        finish();
    }

    @Override
    public void showPswDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(PayFragment.EXTRA_CONTENT, getString(R.string.pay).concat("：") + map.get(IntentConstants.PRICE));
        final PayFragment fragment = new PayFragment();
        fragment.setArguments(bundle);
        fragment.setPaySuccessCallBack(new PayPwdView.InputCallBack() {
            @Override
            public void onInputFinish(String result) {
                String payPwd = StringUtil.getPassword(result);
                mPresenter.checkPayPasswoed(payPwd, fragment, map);
            }
        });
        fragment.show(getSupportFragmentManager(), "MGMPay");
    }

    @Override
    public void showView(PayCheckOrderModel data) {
        mTvPayeeName.setText(data.getName());
        mTvOrderInfo.setText(data.getExtension());
        mTvARPrice.setText(data.getPrice().concat(getString(R.string.ar)));

        map.put(IntentConstants.APPID, data.getAppId());
        map.put(IntentConstants.APPKEY, data.getAppKey());
        map.put(IntentConstants.PREPAYID, data.getPrepayId());
        map.put(IntentConstants.PRICE, data.getPrice());
    }

    @Override
    public void noLogin() {
        startActivity(LoginActivity.mgnPay2Login(this, true));
    }

    @Override
    public void showUserInfo(UserInfoModel data) {
        mPresenter.requestCheckOrder(mOrderString);
        mTvBalance.setText(data.getUMoney());
    }

    @Override
    public void showUserInfoFail(ResponseModel data) {
        result(String.valueOf(data.getCode()), data.getMsg());
    }
}
