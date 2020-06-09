package com.fjx.mg.scan;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.fjx.mg.R;
import com.fjx.mg.me.safe_center.fingerprint.FingerprintActivity;
import com.fjx.mg.me.wallet.recharge.success.SuccessfulShowActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.StatusBarManager;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.db.DBDaoFactory;
import com.library.repository.db.model.FingerprintModel;
import com.library.repository.models.AgentInfoModel;
import com.library.repository.models.UserInfoModel;

import butterknife.BindView;
import butterknife.OnClick;

public class ScanSuccessfulActivity extends BaseMvpActivity<ScanSuccessfulPresenter> implements ScanSuccessfulContract.View {

    @BindView(R.id.tvScanPhone)
    TextView phone;//电话
    @BindView(R.id.tvScanAmount)
    TextView amount;//到账金额
    @BindView(R.id.tvScanPay)
    TextView pay;//支付现金
    @BindView(R.id.tvScanPoundage)
    TextView poundage;//手续费
    @BindView(R.id.tvScanOrderId)
    TextView orderId;//订单号
    private String price;
    private String servicePrice;
    private String payCode;
    private String totalprice;
    private Boolean paySuccess = false;
    private String realPrice;
    private Dialog dialog;

    public static Intent newInstance(Context context, String scan) {
        Intent intent = new Intent(context, ScanSuccessfulActivity.class);
        intent.putExtra("scan", scan);
        return intent;
    }

    @Override
    public FragmentManager fragmentManager() {
        return getSupportFragmentManager();
    }


    @Override
    protected int layoutId() {
        return R.layout.activity_scan_successful;
    }

    @Override
    protected ScanSuccessfulPresenter createPresenter() {
        return new ScanSuccessfulPresenter(this);
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
                mPresenter.chargeUserBalance(totalprice, servicePrice, payCode);
            } else {
                startActivity(FingerprintActivity.newInstance(getCurContext()));
            }
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        IntentFilter filter = new IntentFilter("payfragment");
        registerReceiver(broadcastReceiver, filter);
        StatusBarManager.setLightFontColor(this, R.color.black_33);

        String scan = getIntent().getStringExtra("scan");

        if (StringUtil.isEmpty(scan)) {
            return;
        }
        String others = scan.substring(0, scan.lastIndexOf("-"));
        price = scan.substring(scan.lastIndexOf("-") + 1);
        amount.setText(getString(R.string.scan_amount) + price + getString(R.string.ar));

        String othersx = others.substring(0, others.lastIndexOf("-"));
        servicePrice = others.substring(others.lastIndexOf("-") + 1);
        poundage.setText(getString(R.string.scan_poundage) + servicePrice + getString(R.string.ar));
        totalprice = othersx.substring(othersx.lastIndexOf("-") + 1);
        String otherxx = othersx.substring(0, othersx.lastIndexOf("-"));
        realPrice = othersx.substring(othersx.lastIndexOf("-") + 1);
        pay.setText(getString(R.string.scan_pay) + othersx.substring(othersx.lastIndexOf("-") + 1) + getString(R.string.ar));

        payCode = otherxx.substring(otherxx.lastIndexOf("-") + 1);
        orderId.setText(getString(R.string.scan_orderid) + payCode + getString(R.string.ar));
        orderId.setVisibility(View.GONE);
        mPresenter.getAgentInfo(payCode);
        mPresenter.SendUser(totalprice, servicePrice, payCode, Constant.PayStatus.IN, false);
    }

    @OnClick(R.id.tvScanWithDrawal)
    public void ScanWithDrawal() {
        dialog = new Dialog(getCurActivity());
        dialog.setContentView(R.layout.dialog_qr_layout);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        mPresenter.getUserProfile();
    }

    @Override
    public void DoWithDrawal() {
        mPresenter.chargeUserBalance(totalprice, servicePrice, payCode);
    }

    @OnClick(R.id.imgScanCancle)
    public void ScanScanCancle() {//取消支付
        mPresenter.SendUser(price, servicePrice, payCode, Constant.PayStatus.FAILE, false);
        finish();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        if (!paySuccess) {//取消支付
            mPresenter.SendUser(price, servicePrice, payCode, Constant.PayStatus.FAILE, false);
        }
        super.onDestroy();
    }

    @Override
    public void WorkManSuccess(Boolean paySuccess) {
        this.paySuccess = paySuccess;
        startActivity(SuccessfulShowActivity.newInstance(getCurContext(), price + getString(R.string.ar), realPrice + getString(R.string.ar), "-" + servicePrice + getString(R.string.ar), Constant.SuccessfullShowType.WD));
        finish();
    }

    @Override
    public void ShowAgentInfo(AgentInfoModel info) {
        if (!TextUtils.isEmpty(info.getuNick())) {
            phone.setText(info.getuNick());
        } else {
            phone.setText(info.getPhone());
        }
    }

    /**
     * 判断余额是否大于支付金额
     *
     * @param data
     */
    @Override
    public void showUserInfo(UserInfoModel data) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        if (data == null) {
            CommonToast.toast(R.string.Insufficient_Balance);
            return;
        }
        if (StringUtil.isNotEmpty(data.getUMoney()) && StringUtil.isNotEmpty(totalprice)) {
            int v1 = Integer.parseInt(data.getUMoney());
            int v2 = Integer.parseInt(totalprice);
            if (v1 < v2) {
                CommonToast.toast(R.string.Insufficient_Balance);
            } else {
                mPresenter.showPayPasswordDiaog(totalprice);
            }
        } else {
            CommonToast.toast(R.string.Insufficient_Balance);
        }
    }
}
