package com.fjx.mg.mgmpay;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.fjx.mg.R;
import com.library.common.base.BaseActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.StringUtil;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Author    by hanlz
 * Date      on 2020/3/20.
 * Description：
 */
public class MGMPayResultActivity extends BaseActivity {

    @BindView(R.id.tvPriceAR)
    TextView mTvPriceAR;

    public static Intent newIntent(Context context, String payPrice) {
        Intent intent = new Intent(context, MGMPayResultActivity.class);
        intent.putExtra(IntentConstants.PRICE, payPrice);
        return intent;
    }

    @Override
    protected void initView() {
        super.initView();
        String payPrice = getIntent().getStringExtra(IntentConstants.PRICE);
        if (StringUtil.isNotEmpty(payPrice)) {
            mTvPriceAR.setText(payPrice.concat(getString(R.string.ar)));
        }
    }

    @Override
    protected int layoutId() {
        return R.layout.act_mgm_pay_result;
    }

    @OnClick(R.id.tvConfirm)
    public void onClickView(View view) {
        result("10000", "success");
    }

    private void result(String code, String msg) {
        Intent intent = new Intent("com.fjx.pay_result");
        intent.putExtra(IntentConstants.CODE, code);
        intent.putExtra(IntentConstants.RESULT, msg);
        sendBroadcast(intent);
        finish();
    }
}
