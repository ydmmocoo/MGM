package com.fjx.mg.food;

import android.content.Context;
import android.content.Intent;
import android.widget.ListView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.food.adapter.LvCouponAdapter;
import com.fjx.mg.food.contract.ChooseCouponContract;
import com.fjx.mg.food.presenter.ChooseCouponPresenter;
import com.library.common.base.BaseMvpActivity;
import com.library.repository.models.CouponBean;

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

    private String mId;
    private String mPrice;

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
        ToolBarManager.with(this).setTitle(getString(R.string.coupon))
                .setRightText(getResources().getString(R.string.confirm),R.color.colorAccent, v -> {
                    Intent intent=new Intent();
                    intent.putExtra("cId",mId);
                    intent.putExtra("price",mPrice);
                    setResult(RESULT_OK,intent);
                    finish();
                });
        String price=getIntent().getStringExtra("price");
        String phone=getIntent().getStringExtra("phone");

        mAdapter = new LvCouponAdapter(getCurContext(), mList);
        mLvCoupon.setAdapter(mAdapter);
        //Item点击事件
        mLvCoupon.setOnItemClickListener((parent, view, position, id) -> {
            mAdapter.setSelectPos(position);
            mId=mList.get(position).getCId();
            mPrice=mList.get(position).getPrice();
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
