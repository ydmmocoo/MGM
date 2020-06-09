package com.fjx.mg.nearbycity.pay;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.common.paylibrary.model.UsagePayMode;
import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.library.common.base.BaseActivity;
import com.library.common.constant.IntentConstants;
import com.library.common.utils.GradientDrawableHelper;
import com.library.common.utils.StringUtil;
import com.library.repository.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class NearbyCityPaySuccessActivity extends BaseActivity {


    @BindView(R.id.tvConfirm)
    TextView tvConfirm;
    @BindView(R.id.tvMoney)
    TextView mTvMoney;
    @BindView(R.id.tvCongratulation)
    TextView mTvCongratulation;
    @BindView(R.id.llRoot)
    LinearLayout mLlRoot;
    @BindView(R.id.ivHongbao)
    ImageView mIvHongbao;

    String money;

    public static Intent newInstance(Context context, String money) {
        Intent intent = new Intent(context, NearbyCityPaySuccessActivity.class);
        intent.putExtra(IntentConstants.MONEY, money);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.act_pay_result;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle("");
        GradientDrawableHelper.whit(tvConfirm).setColor(R.color.colorAccent).setCornerRadius(50);
        money = getIntent().getStringExtra(IntentConstants.MONEY);
        if (StringUtil.isNotEmpty(money)) {
            mLlRoot.setVisibility(View.VISIBLE);
            mTvMoney.setVisibility(View.VISIBLE);
            mTvCongratulation.setVisibility(View.VISIBLE);
            mIvHongbao.setVisibility(View.VISIBLE);
            mTvMoney.setText(money.concat(getString(R.string.ar)));
        } else {
            mLlRoot.setVisibility(View.GONE);
            mTvMoney.setVisibility(View.GONE);
            mTvCongratulation.setVisibility(View.GONE);
            mIvHongbao.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.tvConfirm)
    public void onViewClicked() {
        finish();
    }
}
