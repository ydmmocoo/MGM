package com.fjx.mg.food;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.food.contract.ApplyForRefundContract;
import com.fjx.mg.food.presenter.ApplyForRefundPresenter;
import com.fjx.mg.view.RoundImageView;
import com.library.common.base.BaseMvpActivity;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class ApplyForRefundActivity extends BaseMvpActivity<ApplyForRefundPresenter> implements ApplyForRefundContract.View {

    @BindView(R.id.iv_store_logo)
    RoundImageView mIvStoreLogo;
    @BindView(R.id.tv_store_name)
    TextView mTvStoreName;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_price)
    TextView mTvPrice;
    @BindView(R.id.tv_refund_money)
    TextView mTvRefundMoney;
    @BindView(R.id.et_custom_reason)
    EditText mEtCustomReason;
    @BindView(R.id.iv_reason_one)
    ImageView mIvReasonOne;
    @BindView(R.id.iv_reason_two)
    ImageView mIvReasonTwo;
    @BindView(R.id.iv_reason_three)
    ImageView mIvReasonThree;
    @BindView(R.id.iv_reason_four)
    ImageView mIvReasonFour;
    @BindView(R.id.iv_reason_five)
    ImageView mIvReasonFive;

    private String mOrderId, mStoreId;
    private String mRemark;

    @Override
    protected int layoutId() {
        return R.layout.activity_apply_for_refund;
    }

    @Override
    protected void initView() {
        mOrderId = getIntent().getStringExtra("order_id");
        mStoreId = getIntent().getStringExtra("store_id");
        String storeName = getIntent().getStringExtra("store_name");
        String storeLogo = getIntent().getStringExtra("store_logo");
        String goodsName = getIntent().getStringExtra("goods_name");
        String price = getIntent().getStringExtra("price");
        //设置标题
        ToolBarManager.with(this).setTitle(getResources().getString(R.string.apply_for_refund));
        //设置店铺Logo
        mIvStoreLogo.setRectAdius(DimensionUtil.dip2px(5));
        CommonImageLoader.load(storeLogo)
                .placeholder(R.drawable.big_image_default).into(mIvStoreLogo);
        //设置店铺名
        mTvStoreName.setText(storeName);
        //设置商品名
        mTvName.setText(goodsName);
        //设置商品金额
        mTvPrice.setText(getResources().getString(R.string.goods_price, price));
        //设置退款金额
        mTvRefundMoney.setText(getResources().getString(R.string.goods_price, price));
        //默认退款原因
        mRemark=getResources().getString(R.string.no_merchandise_received);

        //监听输入框获取焦点情况
        mEtCustomReason.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                mRemark="";
                mIvReasonOne.setImageResource(R.mipmap.icon_unselected);
                mIvReasonTwo.setImageResource(R.mipmap.icon_unselected);
                mIvReasonThree.setImageResource(R.mipmap.icon_unselected);
                mIvReasonFour.setImageResource(R.mipmap.icon_unselected);
                mIvReasonFive.setImageResource(R.mipmap.icon_unselected);
            }
        });
    }

    @OnClick({R.id.tv_store_name, R.id.iv_reason_one, R.id.iv_reason_two, R.id.iv_reason_three, R.id.iv_reason_four, R.id.iv_reason_five, R.id.tv_submit_application})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_store_name://点击店铺名称跳转店铺
                Intent intent = new Intent(getCurContext(), StoreDetailActivity.class);
                intent.putExtra("id", mStoreId);
                startActivity(intent);
                break;
            case R.id.iv_reason_one:
                mRemark=getResources().getString(R.string.no_merchandise_received);
                mIvReasonOne.setImageResource(R.mipmap.icon_selected);
                mIvReasonTwo.setImageResource(R.mipmap.icon_unselected);
                mIvReasonThree.setImageResource(R.mipmap.icon_unselected);
                mIvReasonFour.setImageResource(R.mipmap.icon_unselected);
                mIvReasonFive.setImageResource(R.mipmap.icon_unselected);
                break;
            case R.id.iv_reason_two:
                mRemark=getResources().getString(R.string.delivery_delay_too_long);
                mIvReasonOne.setImageResource(R.mipmap.icon_unselected);
                mIvReasonTwo.setImageResource(R.mipmap.icon_selected);
                mIvReasonThree.setImageResource(R.mipmap.icon_unselected);
                mIvReasonFour.setImageResource(R.mipmap.icon_unselected);
                mIvReasonFive.setImageResource(R.mipmap.icon_unselected);
                break;
            case R.id.iv_reason_three:
                mRemark=getResources().getString(R.string.wrong_order_or_re_order);
                mIvReasonOne.setImageResource(R.mipmap.icon_unselected);
                mIvReasonTwo.setImageResource(R.mipmap.icon_unselected);
                mIvReasonThree.setImageResource(R.mipmap.icon_selected);
                mIvReasonFour.setImageResource(R.mipmap.icon_unselected);
                mIvReasonFive.setImageResource(R.mipmap.icon_unselected);
                break;
            case R.id.iv_reason_four:
                mRemark=getResources().getString(R.string.forget_to_use_special_red_envelopes);
                mIvReasonOne.setImageResource(R.mipmap.icon_unselected);
                mIvReasonTwo.setImageResource(R.mipmap.icon_unselected);
                mIvReasonThree.setImageResource(R.mipmap.icon_unselected);
                mIvReasonFour.setImageResource(R.mipmap.icon_selected);
                mIvReasonFive.setImageResource(R.mipmap.icon_unselected);
                break;
            case R.id.iv_reason_five:
                mRemark=getResources().getString(R.string.do_not_want_to_eat);
                mIvReasonOne.setImageResource(R.mipmap.icon_unselected);
                mIvReasonTwo.setImageResource(R.mipmap.icon_unselected);
                mIvReasonThree.setImageResource(R.mipmap.icon_unselected);
                mIvReasonFour.setImageResource(R.mipmap.icon_unselected);
                mIvReasonFive.setImageResource(R.mipmap.icon_selected);
                break;
            case R.id.tv_submit_application://提交申请
                String customReason = mEtCustomReason.getText().toString();
                if (TextUtils.isEmpty(customReason)) {
                    if (TextUtils.isEmpty(mRemark)) {
                        CommonToast.toast(getResources().getString(R.string.input_refund_reason));
                        return;
                    }
                } else {
                    mRemark = customReason;
                }
                mPresenter.refuseOrder(mOrderId, mRemark);
                break;
        }
    }

    @Override
    public void refuseOrderSuccess() {
        finish();
    }

    @Override
    protected ApplyForRefundPresenter createPresenter() {
        return new ApplyForRefundPresenter(this);
    }
}