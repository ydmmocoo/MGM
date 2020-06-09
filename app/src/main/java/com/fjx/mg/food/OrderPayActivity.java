package com.fjx.mg.food;

import android.content.Intent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.common.paylibrary.PayConfig;
import com.common.paylibrary.model.UsagePayMode;
import com.common.paylibrary.receiver.PayCallbackReceiver;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.food.contract.OrderPayContract;
import com.fjx.mg.food.presenter.OrderPayPresenter;
import com.fjx.mg.me.wallet.recharge.BalanceRechargeActivity;
import com.library.common.base.BaseMvpActivity;
import com.tencent.qcloud.uikit.common.utils.SoundPlayUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class OrderPayActivity extends BaseMvpActivity<OrderPayPresenter> implements OrderPayContract.View {

    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.cb_balance)
    CheckBox mCbBalance;
    @BindView(R.id.cb_alipay)
    CheckBox mCbAlipay;
    @BindView(R.id.cb_wechat)
    CheckBox mCbWechat;

    private String mPrice;
    private String mOrderId;
    private PayCallbackReceiver payCallbackReceiver;

    @Override
    protected int layoutId() {
        return R.layout.activity_order_pay;
    }

    @Override
    protected void initView() {
        //设置标题
        ToolBarManager.with(this).setTitle(getResources().getString(R.string.payment));
        mPrice=getIntent().getStringExtra("price");
        mOrderId=getIntent().getStringExtra("order_id");
        mTvPrice.setText(getResources().getString(R.string.goods_price,mPrice));

        mPresenter.getUserBalance();

        payCallbackReceiver = PayConfig.registPayReceiver(this, extModel -> {
            SoundPlayUtils.play(2);
            if (extModel.getType() == UsagePayMode.food_pay) {
                Intent intent=new Intent(getCurContext(), OrderActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @OnClick({R.id.tv_recharge, R.id.v_balance, R.id.v_alipay, R.id.v_wechat, R.id.tv_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_recharge://去充值
                startActivityForResult(BalanceRechargeActivity.newInstance(getCurContext()), 111);
                break;
            case R.id.v_balance://余额支付
                mCbBalance.setChecked(true);
                mCbAlipay.setChecked(false);
                mCbWechat.setChecked(false);
                break;
            case R.id.v_alipay://支付宝支付
                mCbBalance.setChecked(false);
                mCbAlipay.setChecked(true);
                mCbWechat.setChecked(false);
                break;
            case R.id.v_wechat://微信支付
                mCbBalance.setChecked(false);
                mCbAlipay.setChecked(false);
                mCbWechat.setChecked(true);
                break;
            case R.id.tv_pay://去支付
                if (mCbAlipay.isChecked()){
                    mPresenter.orderByAlipay(mOrderId);
                }else if (mCbWechat.isChecked()){
                    mPresenter.orderByWechat(mOrderId);
                }else {
                    mPresenter.checkOrder(mPrice,mOrderId);
                }
                break;
        }
    }

    @Override
    public FragmentManager fragmentManager() {
        return getSupportFragmentManager();
    }

    @Override
    public void getBalanceSuccess(String balance) {
        mTvBalance.setText(getResources().getString(R.string.balance_dot).concat(balance));
    }

    @Override
    public void balancePaySuccess() {
        Intent intent=new Intent(getCurContext(), OrderActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected OrderPayPresenter createPresenter() {
        return new OrderPayPresenter(this);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(payCallbackReceiver);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) mPresenter.getUserBalance();
    }
}