package com.fjx.mg.food;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.food.adapter.LvRefundDetailProcessAdapter;
import com.library.common.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 退款详情
 */
public class RefundDetailsActivity extends BaseActivity {

    @BindView(R.id.tv_refund_amount)
    TextView mTvRefundAmount;
    @BindView(R.id.tv_return_account)
    TextView mTvReturnAccount;
    @BindView(R.id.tv_refund_reason)
    TextView mTvRefundReason;
    @BindView(R.id.tv_refund_status)
    TextView mTvRefundStatus;
    @BindView(R.id.lv_process)
    ListView mLvProcess;

    private LvRefundDetailProcessAdapter mAdapter;

    public static Intent newInstance(Context context, String refundStatus, String refundRemark,
                                     String price) {
        Intent intent = new Intent(context, RefundDetailsActivity.class);
        intent.putExtra("refund_status", refundStatus);
        intent.putExtra("refund_remark", refundRemark);
        intent.putExtra("price", price);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_refund_details;
    }

    @Override
    protected void initView() {
        String refundStatus = getIntent().getStringExtra("refund_status");
        String refundRemark = getIntent().getStringExtra("refund_remark");
        String price = getIntent().getStringExtra("price");
        //设置标题
        ToolBarManager.with(this).setTitle(getString(R.string.refund_detail));
        //退款状态  1:同意，2:等待退款,3:拒绝
        String refundStatusText = "";
        if (refundStatus.equals("1")) {
            refundStatusText = getResources().getString(R.string.food_refund_status,
                    getResources().getString(R.string.merchant_agrees_to_refund));
        } else if (refundStatus.equals("2")) {
            refundStatusText = getResources().getString(R.string.food_refund_status,
                    getResources().getString(R.string.refund_processing));
        } else {
            refundStatusText = getResources().getString(R.string.food_refund_status,
                    getResources().getString(R.string.merchant_rejects_refund));
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            //设置退款金额
            mTvRefundAmount.setText(Html.fromHtml(getResources().getString(R.string.food_refund_amount, price), Html.FROM_HTML_MODE_LEGACY));
            //设置退回账户
            mTvReturnAccount.setText(Html.fromHtml(getResources().getString(R.string.food_return_account,
                    getResources().getString(R.string.wallet_balance)), Html.FROM_HTML_MODE_LEGACY));
            //退款原因
            mTvRefundReason.setText(Html.fromHtml(getResources().getString(R.string.food_refund_reason, refundRemark),
                    Html.FROM_HTML_MODE_LEGACY));
            //设置退款状态
            mTvRefundStatus.setText(Html.fromHtml(refundStatusText, Html.FROM_HTML_MODE_LEGACY));
        } else {
            //设置退款金额
            mTvRefundAmount.setText(Html.fromHtml(getResources().getString(R.string.food_refund_amount, price)));
            //设置退回账户
            mTvReturnAccount.setText(Html.fromHtml(getResources().getString(R.string.food_return_account,
                    getResources().getString(R.string.wallet_balance))));
            //退款原因
            mTvRefundReason.setText(Html.fromHtml(getResources().getString(R.string.food_refund_reason, refundRemark)));
            //设置退款状态
            mTvRefundStatus.setText(Html.fromHtml(refundStatusText));
        }

        mAdapter = new LvRefundDetailProcessAdapter(getCurContext());
        mLvProcess.setAdapter(mAdapter);
    }
}
