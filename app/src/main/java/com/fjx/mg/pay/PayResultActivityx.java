package com.fjx.mg.pay;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseActivity;
import com.library.common.utils.GradientDrawableHelper;
import com.library.repository.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class PayResultActivityx extends BaseActivity {

    @BindView(R.id.tvPriceAR)
    TextView tvPriceAR;
    @BindView(R.id.tvPayPrice)
    TextView tvPayPrice;
    @BindView(R.id.llPayPrice)
    LinearLayout llPayPrice;
    @BindView(R.id.tvConfirm)
    TextView tvConfirm;

    public static Intent newInstance(Context context, String arPrice, String price, int payType) {
        Intent intent = new Intent(context, PayResultActivityx.class);
        intent.putExtra("arPrice", arPrice);
        intent.putExtra("price", price);
        intent.putExtra("payType", payType);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_pay_result_activityx;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle("");
        GradientDrawableHelper.whit(tvConfirm).setColor(R.color.colorAccent).setCornerRadius(50);

        String arPrice = getIntent().getStringExtra("arPrice");
        String price = getIntent().getStringExtra("price");
        int payType = getIntent().getIntExtra("payType", Constant.PayType.ACCOUNT);

        if (payType == Constant.PayType.ACCOUNT) {
            llPayPrice.setVisibility(View.GONE);
        } else {
            llPayPrice.setVisibility(View.VISIBLE);
        }

        tvPriceAR.setText(arPrice);
        tvPayPrice.setText(price);

    }

    @OnClick(R.id.tvConfirm)
    public void onViewClicked() {
        finish();
    }
}
