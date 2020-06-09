package com.fjx.mg.food;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.food.adapter.LvRedEnvelopeAndCouponAdapter;
import com.fjx.mg.food.contract.ChooseCouponContract;
import com.fjx.mg.food.presenter.ChooseCouponPresenter;
import com.library.common.base.BaseMvpActivity;
import com.library.repository.models.CouponBean;

import java.util.List;

import butterknife.BindView;

public class RedEnvelopeAndCouponActivity extends BaseMvpActivity<ChooseCouponPresenter> implements ChooseCouponContract.View {

    @BindView(R.id.lv_coupon)
    ListView mLvCoupon;

    private LvRedEnvelopeAndCouponAdapter mAdapter;
    private List<CouponBean.CouponListBean> mList;

    public static Intent newInstance(Context context) {
        return new Intent(context, RedEnvelopeAndCouponActivity.class);
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_red_envelope_and_coupon;
    }

    @Override
    protected void initView() {
        //设置标题
        ToolBarManager.with(this).setTitle(getString(R.string.redpacket_vouchers));

        mAdapter = new LvRedEnvelopeAndCouponAdapter(getCurContext(), mList);
        mLvCoupon.setAdapter(mAdapter);

        mPresenter.getCouponList("","");
    }

    @Override
    protected ChooseCouponPresenter createPresenter() {
        return new ChooseCouponPresenter(this);
    }

    @Override
    public void getCouponListSuccess(List<CouponBean.CouponListBean> data) {
        mList=data;
        mAdapter.setData(mList);
    }
}