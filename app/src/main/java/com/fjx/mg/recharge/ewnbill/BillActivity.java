package com.fjx.mg.recharge.ewnbill;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.paylibrary.model.PayExtModel;
import com.common.paylibrary.model.UsagePayMode;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.pay.PayActivity;
import com.fjx.mg.setting.feedback.FeedBackActivity;
import com.fjx.mg.utils.DialogUtil;
import com.fjx.mg.utils.RankPermissionHelper;
import com.fjx.mg.view.PriceTextWatcher;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonToast;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.JsonUtil;
import com.library.common.utils.SoftInputUtil;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.ServiceModel;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class BillActivity extends BaseMvpActivity<BillPresenter> implements BillContract.View {
    //    交费类别：1为电费，2为水费，3为网费
    public static final int BILL_TYPE_ELEC = 1;
    public static final int BILL_TYPE_WATER = 2;
    public static final int BILL_TYPE_NET = 3;


    @BindView(R.id.tvBuyNow)
    TextView tvBuyNow;
    @BindView(R.id.tvService)
    TextView tvService;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.etMoney)
    EditText etMoney;

    private String serviceName;
    private int type;

    @Override
    protected BillPresenter createPresenter() {
        return new BillPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_water_bill;
    }

    public static Intent newInstance(Context context, int type) {
        Intent intent = new Intent(context, BillActivity.class);
        intent.putExtra("type", type);
        return intent;
    }

    @Override
    protected void initView() {
        type = getIntent().getIntExtra("type", BILL_TYPE_NET);
        String title = getString(R.string.Pay_network_fee);
        if (type == BILL_TYPE_WATER) {
            title = getString(R.string.Pay_water_fee);
        } else if (type == BILL_TYPE_ELEC) {
            title = getString(R.string.Pay_elect_fee);
        }
        ToolBarManager.with(this).setTitle(title);
        GradientDrawableHelper.whit(tvBuyNow).setColor(R.color.colorAccent).setCornerRadius(50);
        mPresenter.getServiceType(type);
        etMoney.addTextChangedListener(new PriceTextWatcher());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SoftInputUtil.showSoftInputView(getCurActivity(), etAccount);
            }
        }, 100);
    }

    @Override
    public void selectService(ServiceModel serviceModel) {
        serviceName = serviceModel.getServiceName();
        tvService.setText(serviceName);

    }


    @OnClick({R.id.llService, R.id.tvBuyNow})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llService:
                mPresenter.showServiceTypeDialog();
                break;
            case R.id.tvBuyNow:
//                if (RankPermissionHelper.NoPrivileges(2)) return;
                if (UserCenter.hasLogin()) {
                    String price = etMoney.getText().toString();
                    String account = etAccount.getText().toString();

                    if (TextUtils.isEmpty(account)) {
                        CommonToast.toast(getString(R.string.hint_recharge_account));
                        return;

                    }

                    if (TextUtils.isEmpty(price)) {
                        CommonToast.toast(getString(R.string.hint_recharge_amount));
                        return;
                    }
                    float e = Float.parseFloat(price);
                    if (e < Constant.limitAmount) {
                        CommonToast.toast(getString(R.string.limit_amount));
                        return;
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("type", type);
                    map.put("service", serviceName);
                    map.put("price", price);
                    map.put("account", account);
                    PayExtModel extModel = new PayExtModel(UsagePayMode.net_recharge, map);
                    startActivity(PayActivity.newInstance(getCurContext(), JsonUtil.moderToString(extModel)));
                } else {
                    new DialogUtil().showAlertDialog(getCurActivity(), R.string.tips, R.string.not_login_forward_login, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            UserCenter.goLoginActivity();
                        }
                    });
                }

                break;
        }
    }
}
