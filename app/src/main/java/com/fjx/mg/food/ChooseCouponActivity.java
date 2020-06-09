package com.fjx.mg.food;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.food.adapter.LvCouponAdapter;
import com.fjx.mg.food.contract.ChooseCouponContract;
import com.fjx.mg.food.presenter.ChooseCouponPresenter;
import com.fjx.mg.me.invite.InviteActivity;
import com.library.common.base.BaseActivity;
import com.library.common.base.BaseMvpActivity;
import com.library.repository.models.CouponBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 选择优惠券
 */
public class ChooseCouponActivity extends BaseMvpActivity<ChooseCouponPresenter> implements ChooseCouponContract.View {

    @BindView(R.id.lv_coupon)
    ListView mLvCoupon;

    private LvCouponAdapter mAdapter;
    private List<CouponBean.CouponListBean> mList;

    public static Intent newInstance(Context context) {
        return new Intent(context, ChooseCouponActivity.class);
    }

    public static Intent newInstance(Context context,String price,String phone) {
        Intent intent=new Intent(context, ChooseCouponActivity.class);
        intent.putExtra("price",price);
        intent.putExtra("phone",phone);
        return intent;
    }

    @Override
    protected int layoutId() {
        return R.layout.activity_choose_coupon;
    }

    @Override
    protected void initView() {
        //设置标题
        ToolBarManager.with(this).setTitle(getString(R.string.coupon));
        String price=getIntent().getStringExtra("price");
        String phone=getIntent().getStringExtra("phone");

        mAdapter = new LvCouponAdapter(getCurContext(), mList);
        mLvCoupon.setAdapter(mAdapter);
        //Item点击事件
        mLvCoupon.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent=new Intent();
            intent.putExtra("cId",mList.get(position).getCId());
            intent.putExtra("price",mList.get(position).getPrice());
            setResult(RESULT_OK,intent);
            finish();
        });

        mPresenter.getCouponList(price,phone);
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
