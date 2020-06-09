package com.fjx.mg.recharge.detail;

import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.recharge.center.RechargeCenterActivityx;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.GradientDrawableHelper;
import com.library.repository.models.RechargeDetailModel;
import com.library.repository.models.RechargePhoneDetailModel;

import butterknife.BindView;
import butterknife.OnClick;

public class RecordDetailActivity extends BaseMvpActivity<RecordDetailPresenter> implements RecordDetailContract.View {

    @BindView(R.id.tvPriceAR)
    TextView tvPriceAR;
    @BindView(R.id.tvPayPrice)
    TextView tvPayPrice;
    @BindView(R.id.tvTypeHint)
    TextView tvTypeHint;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvNumHint)
    TextView tvNumHint;
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.tvPriceHint)
    TextView tvPriceHint;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvServiceFee)
    TextView tvServiceFee;
    @BindView(R.id.llServiceFee)
    LinearLayout llServiceFee;
    @BindView(R.id.tvServiceProvider)
    TextView tvServiceProvider;
    @BindView(R.id.llServiceProvider)
    LinearLayout llServiceProvider;
    @BindView(R.id.tvCreateDate)
    TextView tvCreateDate;
    @BindView(R.id.tvPayType)
    TextView tvPayType;

    @BindView(R.id.tvStatusHint)
    TextView tvStatusHint;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.tvAgain)
    TextView mTvAgain;


    private String id, type;
    private boolean isPhone;
    private RechargePhoneDetailModel mModel;

    public static Intent newInstance(Context context, String id, String type, boolean isPhone) {
        Intent intent = new Intent(context, RecordDetailActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        intent.putExtra("isPhone", isPhone);

        return intent;
    }

    @Override
    protected void initView() {
        ToolBarManager.with(this).setTitle(getString(R.string.Payment_details));
        isPhone = getIntent().getBooleanExtra("isPhone", true);
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");

        if (isPhone) {
            mPresenter.getPhoneRecordDetail(id, type);
        } else {
            mPresenter.getRecordDetail(id, type);
            noPhoneShow();
        }
        GradientDrawableHelper.whit(mTvAgain).setColor(R.color.colorAccent).setCornerRadius(50);
    }

    @Override
    protected RecordDetailPresenter createPresenter() {
        return new RecordDetailPresenter(this);
    }

    @Override
    protected int layoutId() {
        return R.layout.ac_record_detail;
    }

    private void noPhoneShow() {
        tvTypeHint.setText(getString(R.string.Payment_type));
        tvNumHint.setText(getString(R.string.Payment_account));
        tvPriceHint.setText(getString(R.string.Payment_amount));
        tvStatusHint.setText(getString(R.string.rechar_status));
        llServiceFee.setVisibility(View.VISIBLE);
        llServiceProvider.setVisibility(View.VISIBLE);
    }


    @Override
    public void showPhoneDetail(RechargePhoneDetailModel model) {
        mModel = model;
        tvType.setText(model.getType());
        tvPrice.setText(model.getPrice().concat("AR"));
        tvNum.setText(model.getPhone());
        tvCreateDate.setText(model.getDatetime());
        tvPayType.setText(model.getPayType());
        tvPayPrice.setText(model.getCNYPrice());
        tvPriceAR.setText(model.getNum());
        tvStatus.setText(model.getStatusTip());

        //1:处理成功,2:等待处理,3:取消'
        if (TextUtils.equals("3", model.getStatus())) {
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.getContext(), R.color.colorAccent));
        } else if (TextUtils.equals("2", model.getStatus())) {
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.getContext(), R.color.colorGreen));
        } else {
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.getContext(), R.color.textColor));

        }
        if (TextUtils.equals("1", model.getType())) {
            mTvAgain.setVisibility(View.GONE);
        } else {
            mTvAgain.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showDetail(RechargeDetailModel model) {
        tvType.setText(model.getType());
        tvPrice.setText(model.getPrice().concat("AR"));
        tvNum.setText(model.getAccount());
        tvCreateDate.setText(model.getDatetime());
        tvPayType.setText(model.getPayType());
        tvPayPrice.setText(model.getCNYPrice());
        tvServiceFee.setText(model.getServicePrice().concat("AR"));
        tvServiceProvider.setText(model.getService());
        tvPriceAR.setText(model.getPrice().concat("AR"));
        tvStatus.setText(model.getStatusTip());
        if (TextUtils.equals("3", model.getStatus())) {
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.getContext(), R.color.colorAccent));
        } else {
            tvStatus.setTextColor(ContextCompat.getColor(tvStatus.getContext(), R.color.textColor));
        }
    }

    @OnClick(R.id.tvAgain)
    public void again() {
        String phone = mModel.getPhone();
        startActivity(RechargeCenterActivityx.newInstance(this, phone));
    }
}
