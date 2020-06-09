package com.fjx.mg.main.payment.ask.askpay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.common.paylibrary.PayConfig;
import com.common.paylibrary.listener.PayCallback;
import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.receiver.PayCallbackReceiver;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.main.payment.AskPayResultActivity;
import com.fjx.mg.me.safe_center.SafeCenterActivity;
import com.fjx.mg.me.safe_center.fingerprint.FingerprintActivity;
import com.fjx.mg.me.wallet.recharge.BalanceRechargeActivity;
import com.fjx.mg.utils.SharedPreferencesHelper;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.FingerprintModel;
import com.library.repository.models.UserInfoModel;
import com.tencent.qcloud.uikit.common.utils.SoundPlayUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class AskPayActivity extends BaseMvpActivity<AskPayPresenter> implements AskPayContract.View {
    @BindView(R.id.tvBuyNow)
    TextView tvBuyNow;

    @BindView(R.id.tvPayPrice)
    TextView tvPayPrice;

    @BindView(R.id.tvARPrice)
    TextView tvARPrice;

    @BindView(R.id.tvBalance)
    TextView tvBalance;

    @BindView(R.id.tvGoRecharge)
    TextView tvGoRecharge;

    @BindView(R.id.cbBalancePay)
    CheckBox cbBalancePay;

    @BindView(R.id.cbAliPay)
    CheckBox cbAliPay;

    @BindView(R.id.cbWXPay)
    CheckBox cbWXPay;

    @BindView(R.id.llPayPrice)
    LinearLayout llPayPrice;

    @BindView(R.id.llBalancePay)
    LinearLayout llBalancePay;

    private double myBalance, totalPriceAR;
    PayCallbackReceiver payCallbackReceiver;
    private int payType = Constant.PayType.ACCOUNT;
    private PayExtModel paymodel;
    Map<String, Object> payMap;


    public static Intent newInstance(Context context, String ext) {
        Intent intent = new Intent(context, AskPayActivity.class);
        intent.putExtra("ext", ext);
        return intent;
    }


    @Override
    protected AskPayPresenter createPresenter() {
        return new AskPayPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_ask_pay;
    }

    @Override
    public FragmentManager fragmentManager() {
        return getSupportFragmentManager();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {//密码支付弹窗点击指纹支付
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isFingerEnable = isFingerEnable();
            if (isFingerEnable) {
                mPresenter.setQuestionPriceBy(payType, payMap);
            } else {
                startActivity(FingerprintActivity.newInstance(getCurContext()));
            }
        }
    };

    @Override
    protected void initView() {
        String ext = getIntent().getStringExtra("ext");

        paymodel = JsonUtil.strToModel(ext, PayExtModel.class);
        payMap = JsonUtil.cast(paymodel.getMessage());

        IntentFilter filter = new IntentFilter("payfragment");
        registerReceiver(broadcastReceiver, filter);
        ToolBarManager.with(this).setTitle(getString(R.string.payment));
        GradientDrawableHelper.whit(tvBuyNow).setColor(R.color.colorAccent).setCornerRadius(50);
        mPresenter.getUserBalance();
        payCallbackReceiver = PayConfig.registPayReceiver(this, new PayCallback() {
            @Override
            public void payResult(PayExtModel extModel) {
                SoundPlayUtils.play(2);//支付状态展示
                String arPrice = tvARPrice.getText().toString();
                String price = tvPayPrice.getText().toString();
                startActivity(AskPayResultActivity.newInstance(getCurContext(), arPrice, price, payType));
                CommonToast.toast(getString(R.string.pay_success));
                setResult(111);
                finish();
            }
        });
        String price = (String) payMap.get("price");
        Log.e("x3", "" + price);
        tvARPrice.setText(price.concat("AR"));
        mPresenter.convertMoney(payMap);
    }

    private boolean isFingerEnable() {//应该是检测是否开启指纹支付？
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel == null) return false;
        String uid = infoModel.getUId();
        FingerprintModel model = DBDaoFactory.getFingerprintDao().queryModel(uid);
        if (model == null) return false;
        return model.getFingerEnable();
    }

    @Override
    public void convertMoneySuccess(String price, String servicePrice, String totalPrice) {//换算接口成功
        Log.e("x1", "" + totalPrice);
        tvPayPrice.setText(price);
        tvARPrice.setText(totalPrice.endsWith(".0") ? totalPrice.substring(0, totalPrice.length() - 2).concat("AR") : totalPrice.concat("AR"));


        totalPriceAR = Double.parseDouble(totalPrice);
    }

    @Override
    public void getBalanceSuccess(String balance) {//获取余额成功
        SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
        sp.putString("balance", balance);
        sp.close();
        try {
            this.myBalance = Double.parseDouble(balance);
        } catch (Exception w) {
            Log.d("getBalanceSuccess", w.getMessage());
        }

        tvBalance.setText(getString(R.string.balance_dot).concat(balance).concat("AR"));
    }


    @OnClick(R.id.tvGoRecharge)
    public void onViewRecharge() {//充值余额
        startActivityForResult(BalanceRechargeActivity.newInstance(getCurContext()), 111);
    }

    @OnClick(R.id.tvBuyNow)
    public void onViewClicked() {//点击支付按钮
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (!infoModel.hasNecessaryConditions()) {
            CommonToast.toast(getString(R.string.hint_complete_unfo));
            startActivity(SafeCenterActivity.newInstance(getCurContext()));
            return;
        }

        Log.e("myBalance:" + myBalance, "totalPriceAR:" + totalPriceAR);
        if (payType == Constant.PayType.ACCOUNT) {
            if (myBalance < totalPriceAR) {//并没有完善对比余额是否足够
                startActivityForResult(BalanceRechargeActivity.newInstance(getCurContext()), 111);
                CommonToast.toast(getString(R.string.Insufficient_Balance));
                return;
            }
        }

        mPresenter.setQuestionPriceBy(payType, payMap);

    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(broadcastReceiver);
        if (payCallbackReceiver != null) {
            unregisterReceiver(payCallbackReceiver);
        }
        super.onDestroy();
    }


    @OnClick({R.id.llBalancePay, R.id.llAliPay, R.id.llWXPay})
    public void onViewClicked(View view) {//3种支付方式的切换
        cbBalancePay.setChecked(false);
        cbAliPay.setChecked(false);
        cbWXPay.setChecked(false);
        switch (view.getId()) {
            case R.id.llBalancePay:
                llPayPrice.setVisibility(View.GONE);
                tvGoRecharge.setVisibility(View.VISIBLE);
                cbBalancePay.setChecked(true);
                payType = Constant.PayType.ACCOUNT;
                break;
            case R.id.llAliPay:
                tvGoRecharge.setVisibility(View.GONE);
                llPayPrice.setVisibility(View.VISIBLE);
                cbAliPay.setChecked(true);
                payType = Constant.PayType.ZFB;
                break;
            case R.id.llWXPay:
                llPayPrice.setVisibility(View.VISIBLE);
                tvGoRecharge.setVisibility(View.GONE);
                cbWXPay.setChecked(true);
                payType = Constant.PayType.WX;
                break;


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) mPresenter.getUserBalance();
    }

}
