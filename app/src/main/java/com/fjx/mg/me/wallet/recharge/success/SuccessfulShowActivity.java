package com.fjx.mg.me.wallet.recharge.success;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseMvpActivity;
import com.library.repository.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class SuccessfulShowActivity extends BaseMvpActivity<SuccessfulShowPresenter> implements SuccessfulShowContract.View {

    @BindView(R.id.tvSuccessfulShowTip)
    TextView tip;//提示词
    @BindView(R.id.tvSuccessfulShowAmount)
    TextView amount;//到账金额
    @BindView(R.id.tvSuccessfulShowTotalAmount)
    TextView totalAmount;//实付金额
    @BindView(R.id.tvSuccessfulShowTotalPoundage)
    TextView poundage;//手续费
    boolean flag;

    @Override
    protected SuccessfulShowPresenter createPresenter() {
        return new SuccessfulShowPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_successful_show;
    }

    public static Intent newInstance(Context context, String amount, String totalAmount, String poundage, int payType) {
        Intent intent = new Intent(context, SuccessfulShowActivity.class);
        intent.putExtra("amount", amount);
        intent.putExtra("totalAmount", totalAmount);
        intent.putExtra("poundage", poundage);
        intent.putExtra("payType", payType);
        return intent;
    }

    public static Intent newInstance(Context context, String amount, String totalAmount, String poundage, int payType, boolean flag) {
        Intent intent = new Intent(context, SuccessfulShowActivity.class);
        intent.putExtra("amount", amount);
        intent.putExtra("totalAmount", totalAmount);
        intent.putExtra("poundage", poundage);
        intent.putExtra("payType", payType);
        intent.putExtra("flag", flag);
        return intent;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle("");
        mPresenter.ShowMessage(getIntent().getStringExtra("amount"), getIntent().getStringExtra("totalAmount"), getIntent().getStringExtra("poundage"), getIntent().getIntExtra("payType", Constant.SuccessfullShowType.WD));
        if (getIntent() != null) {
            flag = getIntent().getBooleanExtra("flag", false);
        }
    }

    @OnClick(R.id.tvSuccessfulShow)
    public void onViewClicked() {
        finish();
    }


    @Override
    public void ShowMessage(String am, String tot, String pou, String tips, int payType) {
        amount.setText(am);
        poundage.setText(pou);
        totalAmount.setText(tot);
        tip.setText(tips);
    }
}
