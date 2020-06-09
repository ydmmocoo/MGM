package com.fjx.mg.pay;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.paylibrary.PayConfig;
import com.common.paylibrary.listener.PayCallback;
import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.common.paylibrary.receiver.PayCallbackReceiver;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.me.safe_center.fingerprint.FingerprintActivity;
import com.fjx.mg.me.wallet.recharge.BalanceRechargeActivity;
import com.fjx.mg.recharge.ewnbill.BillActivity;
import com.fjx.mg.scan.ShopPaySuccessEvent;
import com.fjx.mg.utils.RankPermissionHelper;
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

import org.greenrobot.eventbus.EventBus;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PayActivity extends BaseMvpActivity<PayPresenter> implements PayContract.View {
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
    @BindView(R.id.cbBankPay)
    CheckBox cbBankPay;
    Map<String, Object> payMap;
    @BindView(R.id.tvOriginPriceType)
    TextView tvOriginPriceType;
    @BindView(R.id.tvOriginPrice)
    TextView tvOriginPrice;
    @BindView(R.id.tvServiceFee)
    TextView tvServiceFee;
    @BindView(R.id.llOriginPrice)
    LinearLayout llOriginPrice;
    @BindView(R.id.llServiceFee)
    LinearLayout llServiceFee;
    @BindView(R.id.llPayPrice)
    LinearLayout llPayPrice;

    @BindView(R.id.llBalancePay)
    LinearLayout llBalancePay;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;
    @BindView(R.id.tvService)
    TextView tvService;
    @BindView(R.id.llService)
    LinearLayout llService;
    private int payType = Constant.PayType.ACCOUNT;


    private PayExtModel payExtModel;
    PayCallbackReceiver payCallbackReceiver;
    private String WENprice;//水电网充值价格
    private double myBalance, totalPriceAR;
    private Boolean PayS = false;
    public static final String broadcast_codepay = "jason.broadcast.codepay";

    public static Intent newInstance(Context context, String ext) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("ext", ext);
        return intent;
    }

    public static Intent newInstance(Context context, String ext, String title) {
        Intent intent = new Intent(context, PayActivity.class);
        intent.putExtra("ext", ext);
        intent.putExtra("title", title);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_pay;
    }

    private boolean isFingerEnable() {
        UserInfoModel infoModel = UserCenter.getUserInfo();
        if (infoModel == null) return false;
        String uid = infoModel.getUId();
        FingerprintModel model = DBDaoFactory.getFingerprintDao().queryModel(uid);
        if (model == null) return false;
        return model.getFingerEnable();
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean isFingerEnable = isFingerEnable();
            if (isFingerEnable) {
                paytype();
            } else {
                startActivity(FingerprintActivity.newInstance(getCurContext()));
            }
        }
    };

    @Override
    protected void initView() {
        IntentFilter filter = new IntentFilter("payfragment");
        registerReceiver(broadcastReceiver, filter);
        String title = getIntent().getStringExtra("title");
        ToolBarManager.with(this).setTitle(title == null || title.equals("") ? getString(R.string.payment) : title);
        GradientDrawableHelper.whit(tvBuyNow).setColor(R.color.colorAccent).setCornerRadius(50);
        SharedPreferencesHelper sp = new SharedPreferencesHelper(this);
        String string = sp.getString("balance");
        if (string != null && !string.equals("")) {
            try {
                this.myBalance = Double.parseDouble(string);
            } catch (Exception w) {
                Log.d("getBalanceSuccess", w.getMessage());
            }

            tvBalance.setText(getString(R.string.balance_dot).concat(string).concat("AR"));
        }
        sp.close();
        mPresenter.getUserBalance();
        final String ext = getIntent().getStringExtra("ext");
        payExtModel = JsonUtil.strToModel(ext, PayExtModel.class);
        if (payExtModel == null) {
            CommonToast.toast(R.string.params_error);
            finish();
            return;
        }

        payCallbackReceiver = PayConfig.registPayReceiver(this, new PayCallback() {
            @Override
            public void payResult(PayExtModel extModel) {
                SoundPlayUtils.play(2);
                if (extModel.getType() == UsagePayMode.agent_shop) {
                    String arPrice = tvARPrice.getText().toString();
                    String price = tvPayPrice.getText().toString();
                    startActivity(PayResultActivityx.newInstance(getCurContext(), arPrice, price, payType));
                    CommonToast.toast(getString(R.string.pay_success));
                    EventBus.getDefault().post(new ShopPaySuccessEvent());
                    finish();
                    return;
                }
                switch (extModel.getType()) {
                    case phone_recharge:
                    //case net_recharge:
                    case water_recharge:
                    case elect_recharge:
                    case data_recharge:
                        String arPrice = tvARPrice.getText().toString();
                        String price = tvPayPrice.getText().toString();
                        startActivity(PayResultActivity.newInstance(getCurContext(), arPrice, price, payType));
                        break;
                }
                String codes = (String) payMap.get("payCode");
                if (codes != null) {
                    if (!codes.equals("")) {
                        Intent intent = new Intent(broadcast_codepay);
                        intent.putExtra("code", (String) payMap.get("payCode"));
                        intent.putExtra("amount", (String) payMap.get("amount"));
                        intent.putExtra("status", "1");
                        sendBroadcast(intent);
                        String arPrice = tvARPrice.getText().toString();
                        String price = tvPayPrice.getText().toString();
                        startActivity(PayResultActivityx.newInstance(getCurContext(), arPrice, price, payType));
                    }
                }
                PayS = true;
                SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(PayActivity.this);
                sharedPreferencesHelper.putBoolean("paystatus", PayS);
                sharedPreferencesHelper.close();
                CommonToast.toast(getString(R.string.pay_success));
                setResult(111);
                finish();
            }
        });

        payMap = JsonUtil.cast(payExtModel.getMessage());
        switch (payExtModel.getType()) {
            case im_transfer:
            case im_redpacket:
            case mg_transfer:
            case agent_shop:
                //转账
                String money = (String) payMap.get("amount");
                Log.e("x4", "" + money);
                tvARPrice.setText(money.concat("AR"));
                mPresenter.convertMoney("1", money);
                break;
            case recharge_balance:
                //充值余额，不显示余额支付选项
                payType = Constant.PayType.ZFB;
                cbAliPay.setChecked(true);
                llBalancePay.setVisibility(View.GONE);
                tvGoRecharge.setVisibility(View.GONE);
                llPayPrice.setVisibility(View.VISIBLE);
            case phone_recharge:
            case data_recharge:

                //手机流量充值,余额充值
                String price = (String) payMap.get("price");
                Log.e("x3", "" + price);
                tvARPrice.setText(price.concat("AR"));
                mPresenter.convertMoney("1", price);
                break;

            case net_recharge:
            case water_recharge:
            case elect_recharge:
                //水电网账单充值
                llAccount.setVisibility(View.VISIBLE);
                llService.setVisibility(View.VISIBLE);
                String service = (String) payMap.get("service");
                String account = (String) payMap.get("account");
                tvAccount.setText(account);
                tvService.setText(service);

                WENprice = (String) payMap.get("price");
                double type = (double) payMap.get("type");
                tvOriginPrice.setText(WENprice.concat("AR"));
                //先算出服务费
                mPresenter.convertMoney("2", WENprice);
                if (type == BillActivity.BILL_TYPE_ELEC) {
                    tvOriginPriceType.setText(getString(R.string.eectricity_bill));
                } else if (type == BillActivity.BILL_TYPE_WATER) {
                    tvOriginPriceType.setText(getString(R.string.water_bill));
                } else {
                    tvOriginPriceType.setText(getString(R.string.net_bill));
                }
                break;

            case levle_recharge:
                price = (String) payMap.get("price");
                mPresenter.convertMoney("1", price);
                break;

            case three_scan_pay:
                String m = (String) payMap.get("amount");
                tvARPrice.setText(m.concat("AR"));
                mPresenter.convertMoney((String) payMap.get("type"), m);
                break;
            case im_group_red_packet://群红包
                tvARPrice.setText(((String) payMap.get("amount")).concat("AR"));
                mPresenter.convertMoney("1", ((String) payMap.get("amount")));
                break;
        }
    }


    @Override
    protected PayPresenter createPresenter() {
        return new PayPresenter(this);
    }

    @Override
    public FragmentManager fragmentManager() {
        return getSupportFragmentManager();
    }

    @Override
    public void convertMoneySuccess(String type, String price, String servicePrice, String totalPrice) {
        Log.e("x1", "" + totalPrice);
        try {
        } catch (Exception e) {

        }
        switch (payExtModel.getType()) {
            case im_transfer:
            case im_redpacket:
            case mg_transfer:
            case phone_recharge:
            case data_recharge:
            case recharge_balance:
            case levle_recharge:
                tvPayPrice.setText(price);
                tvARPrice.setText(totalPrice.endsWith(".0") ? totalPrice.substring(0, totalPrice.length() - 2).concat("AR") : totalPrice.concat("AR"));
                break;
            case net_recharge:
            case water_recharge:
            case elect_recharge:
                llOriginPrice.setVisibility(View.VISIBLE);
                llServiceFee.setVisibility(View.VISIBLE);
                tvPayPrice.setText(price);
                tvServiceFee.setText(servicePrice.concat("AR"));
                Log.e("x2", "" + totalPrice);
                tvARPrice.setText(totalPrice.concat("AR"));
                payMap.put("servicePrice", servicePrice);
                break;
        }

    }

    @Override
    public void transferSuccess(Object obj) {
    }

    @Override
    public void getBalanceSuccess(String balance) {
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

    @Override
    public double getBalance() {
        return myBalance;
    }


    @OnClick(R.id.tvGoRecharge)
    public void onViewRecharge() {
        startActivityForResult(BalanceRechargeActivity.newInstance(getCurContext()), 111);
    }

    @OnClick(R.id.tvBuyNow)
    public void onViewClicked() {
        if (RankPermissionHelper.setPayPsw()) return;

        if (payType == Constant.PayType.ACCOUNT) {
            if (myBalance < totalPriceAR) {
                startActivityForResult(BalanceRechargeActivity.newInstance(getCurContext()), 111);
                CommonToast.toast(getString(R.string.Insufficient_Balance));
                return;
            }
        }

        paytype();

    }

    private void paytype() {
        switch (payExtModel.getType()) {
            case im_redpacket:
            case im_transfer:
                //聊天转账发红包
                mPresenter.finalTransferPay(payType, payMap);
                break;

            case phone_recharge:
            case data_recharge:
                //手机流量充值
                mPresenter.finalPhoneRechargePay(payType, payMap);
                break;
            case mg_transfer:
                //平台直接转账
                mPresenter.finalTransferDirecPay(payType, payMap);
                break;
            case net_recharge:
            case water_recharge:
            case elect_recharge:
                //水电网费充值
                mPresenter.finalEWNnRechargePay(payType, payMap);
                break;
            case recharge_balance:
                mPresenter.finalbalanceRechargePay(payType, payMap);
                break;
            case levle_recharge:
                mPresenter.finalUpgradePay(payType, payMap);
                break;
            case agent_shop:
                mPresenter.payAgentShop(payType, payMap);
                break;
            case three_scan_pay:
                mPresenter.userThreeScanPay(payType,payMap);
                break;
            case im_group_red_packet://群红包
                mPresenter.payGroupRedPacket(payType,payMap);
                break;
            default:
        }
    }

    @Override
    protected void onDestroy() {

        unregisterReceiver(broadcastReceiver);
        if (payCallbackReceiver != null) {
            unregisterReceiver(payCallbackReceiver);
        }
        super.onDestroy();
    }


    @OnClick({R.id.llBalancePay, R.id.llAliPay, R.id.llWXPay, R.id.llBankPay})
    public void onViewClicked(View view) {
        defaultSelect();
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
            case R.id.llBankPay:
                llPayPrice.setVisibility(View.VISIBLE);
                tvGoRecharge.setVisibility(View.GONE);
                cbBankPay.setChecked(true);
                payType = Constant.PayType.BANKCARD;
                break;

        }
    }

    private void defaultSelect() {
        cbBalancePay.setChecked(false);
        cbAliPay.setChecked(false);
        cbWXPay.setChecked(false);
        cbBankPay.setChecked(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 111) mPresenter.getUserBalance();
    }

}
