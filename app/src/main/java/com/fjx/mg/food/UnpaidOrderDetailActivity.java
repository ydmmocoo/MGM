package com.fjx.mg.food;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.food.adapter.LvOrderDetailGoodsAdapter;
import com.fjx.mg.food.contract.OrderDetailContract;
import com.fjx.mg.food.presenter.OrderDetailPresenter;
import com.gyf.immersionbar.ImmersionBar;
import com.library.common.base.BaseMvpActivity;
import com.library.common.view.WrapContentListView;
import com.library.repository.models.OrderDetailBean;
import com.lxj.xpopup.XPopup;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UnpaidOrderDetailActivity extends BaseMvpActivity<OrderDetailPresenter> implements OrderDetailContract.View {

    @BindView(R.id.tv_pay_value)
    TextView mTvPayValue;
    @BindView(R.id.tv_count_down)
    TextView mTvCountDown;
    @BindView(R.id.tv_store_name)
    TextView mTvStoreName;
    @BindView(R.id.lv_goods)
    WrapContentListView mLvGoods;
    @BindView(R.id.tv_packing_fee)
    TextView mTvPackingFee;
    @BindView(R.id.tv_delivery_fee)
    TextView mTvDeliveryFee;
    @BindView(R.id.tv_red_envelopes)
    TextView mTvRedEnvelopes;
    @BindView(R.id.tv_coupon)
    TextView mTvCoupon;
    @BindView(R.id.tv_total_price)
    TextView mTvTotalPrice;
    @BindView(R.id.tv_receiving_information)
    TextView mTvReceivingInformation;
    @BindView(R.id.tv_expected_time)
    TextView mTvExpectedTime;
    @BindView(R.id.tv_distribution_service)
    TextView mTvDistributionService;
    @BindView(R.id.tv_order_number)
    TextView mTvOrderNumber;
    @BindView(R.id.tv_order_time)
    TextView mTvOrderTime;
    @BindView(R.id.tv_pay_type)
    TextView mTvPayType;

    private LvOrderDetailGoodsAdapter mAdapter;
    private List<OrderDetailBean.OrderInfoBean.GoodsListBean> mList;

    private String mOrderId;
    private String mStoreId;
    private String mPrice;

    private CountDownTimer mTimer;

    @Override
    protected int layoutId() {
        return R.layout.activity_unpaid_order_detail;
    }

    @Override
    protected void initView() {
        ImmersionBar.with(this)
                .fitsSystemWindows(false)
                .transparentStatusBar()
                .statusBarDarkFont(true)
                .init();
        mOrderId = getIntent().getStringExtra("id");
        mStoreId = getIntent().getStringExtra("store_id");

        //初始化商品列表
        mAdapter = new LvOrderDetailGoodsAdapter(getCurContext(), mList);
        mLvGoods.setAdapter(mAdapter);

        mPresenter.getOrderDetail(mOrderId);
    }

    @Override
    public void getOrderDetailSuccess(OrderDetailBean data) {
        //设置倒计时
        if (data.getOrderInfo().getExpireTime()>0) {
            countDown(data.getOrderInfo().getExpireTime());
        }else {
            mTvCountDown.setText(getResources().getString(R.string.pay_count_down,"00:00"));
        }
        //设置店铺名
        mTvStoreName.setText(data.getOrderInfo().getShopName());
        //店铺电话
        //mStorePhone=data.getOrderInfo().getTels();
        //设置商品
        mAdapter.setData(data.getOrderInfo().getGoodsList());
        //设置包装费
        mTvPackingFee.setText(getResources().getString(R.string.goods_price, "0"));
        //设置配送费
        mTvDeliveryFee.setText(getResources().getString(R.string.goods_price,
                data.getOrderInfo().getDeliveryPrice()));
        //设置红包金额
        mTvRedEnvelopes.setText(getResources().getString(R.string.red_envelopes_value,
                data.getOrderInfo().getRedRrice()));
        //设置优惠券金额
        mTvCoupon.setText(getResources().getString(R.string.goods_price, "0"));
        //合计
        mPrice=data.getOrderInfo().getTotalPrice();
        mTvPayValue.setText(getResources().getString(R.string.pay_value)
                .concat(mPrice)
                .concat("AR"));
        mTvTotalPrice.setText(getResources().getString(R.string.goods_price,
                mPrice));
        //设置收货信息
        mTvReceivingInformation.setText(data.getOrderInfo().getAddress().getName().concat(" ")
                .concat(data.getOrderInfo().getAddress().getPhone()).concat("\n")
                .concat(data.getOrderInfo().getAddress().getAddress()));
        //设置期望送达时间
        mTvExpectedTime.setText(data.getOrderInfo().getExpectedDeliveryTime());
        //设置配送服务
        mTvDistributionService.setText(getResources().getString(R.string.merchant_distribution));
        //设置订单号码
        mTvOrderNumber.setText(data.getOrderInfo().getOrderId());
        //设置下单时间
        mTvOrderTime.setText(data.getOrderInfo().getCreateTime());
        //设置付款方式
        if (!TextUtils.isEmpty(data.getOrderInfo().getPayType())) {
            String payType;
            if (data.getOrderInfo().getPayType().equals("2")) {
                payType = getResources().getString(R.string.ali_apy);
            } else if (data.getOrderInfo().getPayType().equals("3")) {
                payType = getResources().getString(R.string.wechat_pay);
            } else {
                payType = getResources().getString(R.string.balance_pay);
            }
            mTvPayType.setText(payType);
        }
    }

    @Override
    public void confirmOrderSuccess() {
    }

    @Override
    public void cancelOrderSuccess() {

    }

    @Override
    public void cancelRefundSuccess() {
    }

    @Override
    public void reBuySuccess(String storeId) {
    }

    @Override
    protected OrderDetailPresenter createPresenter() {
        return new OrderDetailPresenter(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_pay_right, R.id.tv_cancel, R.id.tv_store_name, R.id.iv_call})
    public void onViewClicked(View view) {
        Intent intent=null;
        switch (view.getId()) {
            case R.id.iv_back://返回
                finish();
                break;
            case R.id.tv_pay_right://立即支付
                intent = new Intent(getCurContext(), OrderPayActivity.class);
                intent.putExtra("order_id", mOrderId);
                intent.putExtra("price", mPrice);
                startActivity(intent);
                break;
            case R.id.tv_cancel://取消订单
                new XPopup.Builder(getCurContext())
                        .asConfirm(getResources().getString(R.string.Tips), getResources().getString(R.string.cancel_order_tip),
                                getResources().getString(R.string.cancel), getResources().getString(R.string.confirm_short),
                                () -> mPresenter.cancelOrder(mOrderId), null, false)
                        .show();
                break;
            case R.id.tv_store_name://点击店铺名字跳转详情
                intent = new Intent(getCurContext(), StoreDetailActivity.class);
                intent.putExtra("id", mStoreId);
                startActivity(intent);
                break;
            case R.id.iv_call://拨打电话
                new XPopup.Builder(getCurContext())
                        .asConfirm(getResources().getString(R.string.Tips), getResources().getString(R.string.hint_confirm_contact),
                                getResources().getString(R.string.cancel), getResources().getString(R.string.confirm_short),
                                () -> {
                                    /*Intent callIntent = new Intent(Intent.ACTION_CALL);
                                    Uri data = Uri.parse("tel:" + );
                                    callIntent.setData(data);
                                    startActivity(callIntent);*/
                                }, null, false)
                        .show();
                break;
        }
    }

    /**
     * 倒计时显示
     */
    private void countDown(long time) {
        mTimer = new CountDownTimer(time*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int second= (int) (millisUntilFinished/1000%60);
                int min= (int) (millisUntilFinished/60000%60);
                String minText;
                String secondText;
                if (min<10){
                    minText="0".concat(String.valueOf(min));
                }else {
                    minText=String.valueOf(min);
                }
                if (second<10){
                    secondText="0".concat(String.valueOf(second));
                }else {
                    secondText=String.valueOf(second);
                }
                String time=minText.concat(":").concat(secondText);
                mTvCountDown.setText(getResources().getString(R.string.pay_count_down,time));
            }

            @Override
            public void onFinish() {
                mTvCountDown.setText(getResources().getString(R.string.pay_count_down,"00:00"));
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        if (mTimer!=null){
            mTimer.cancel();
        }
        super.onDestroy();
    }
}