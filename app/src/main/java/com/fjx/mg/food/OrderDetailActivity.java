package com.fjx.mg.food;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.fjx.mg.R;
import com.fjx.mg.ToolBarManager;
import com.fjx.mg.dialog.ShareDialog2;
import com.fjx.mg.food.adapter.LvOrderDetailGoodsAdapter;
import com.fjx.mg.food.contract.OrderDetailContract;
import com.fjx.mg.food.presenter.OrderDetailPresenter;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.library.common.base.BaseMvpActivity;
import com.library.common.view.WrapContentListView;
import com.library.repository.Constant;
import com.library.repository.data.UserCenter;
import com.library.repository.models.OrderDetailBean;
import com.library.repository.models.UserInfoModel;
import com.library.repository.repository.RepositoryFactory;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderDetailActivity extends BaseMvpActivity<OrderDetailPresenter> implements OrderDetailContract.View {

    @BindView(R.id.v_status_waiting_for_order)
    View mVStatusWaitingForOrder;
    @BindView(R.id.iv_checkout_success)
    ImageView mIvCheckoutSuccess;
    @BindView(R.id.iv_waiting_for_order)
    ImageView mIvWaitingForOrder;
    @BindView(R.id.v_status_distribution)
    View mVStatusDistribution;
    @BindView(R.id.iv_distribution_in_progress)
    ImageView mIvDistributionInProgress;
    @BindView(R.id.v_status_complete)
    View mVStatusComplete;
    @BindView(R.id.iv_complete)
    ImageView mIvComplete;
    @BindView(R.id.tv_distribution_in_progress)
    TextView mTvDistributionInProgress;
    @BindView(R.id.tv_complete)
    TextView mTvComplete;
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
    @BindView(R.id.tv_refund_status)
    TextView mTvRefundStatus;
    @BindView(R.id.rl_refund)
    RelativeLayout mRlRefund;
    @BindView(R.id.tv_left)
    TextView mTvLeft;
    @BindView(R.id.tv_right)
    TextView mTvRight;
    @BindView(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @BindView(R.id.v_bottom_line)
    View mVBottomLine;
    @BindView(R.id.tv_checkout_success)
    TextView mTvCheckoutSuccess;
    @BindView(R.id.tv_waiting_for_order)
    TextView mTvWaitingForOrder;
    @BindView(R.id.cl_order_status)
    ConstraintLayout mClOrderStatus;
    @BindView(R.id.tv_shop_full_reduction)
    TextView mTvShopFullReduction;
    @BindView(R.id.tv_first_reduction)
    TextView mTvFirstReduction;
    @BindView(R.id.tv_shop_full_reduction_text)
    TextView mTvShopFullReductionText;
    @BindView(R.id.tv_first_reduction_text)
    TextView mTvFirstReductionText;
    @BindView(R.id.tv_red_envelopes_text)
    TextView mTvRedEnvelopesText;
    @BindView(R.id.tv_distribution_information)
    TextView mTvDistributionInformation;
    @BindView(R.id.tv_receiving_information_text)
    TextView mTvReceivingInformationText;
    @BindView(R.id.tv_expected_time_text)
    TextView mTvExpectedTimeText;
    @BindView(R.id.tv_distribution_service_text)
    TextView mTvDistributionServiceText;

    private LvOrderDetailGoodsAdapter mAdapter;
    private List<OrderDetailBean.OrderInfoBean.GoodsListBean> mList;

    private String mOrderId;
    private String mOId;
    private String mStoreId;
    private String mOrderStatus;
    private String mRefundStatus;
    private String mRefundRemark;
    private String mPayType;
    private String mPrice;
    private String mStoreName;
    private String mStoreLogo;
    private String mGoodsName;

    private String mShareTitle, mShareDesc;
    private List<String> mPhoneList = new ArrayList<>();

    @Override
    protected int layoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initView() {
        //设置标题
        ToolBarManager.with(this).setTitle(getString(R.string.order_detail));
        mOId = getIntent().getStringExtra("id");
        mStoreId = getIntent().getStringExtra("store_id");

        //初始化商品列表
        mAdapter = new LvOrderDetailGoodsAdapter(getCurContext(), mList);
        mLvGoods.setAdapter(mAdapter);

        mPresenter.getOrderDetail(mOId);
    }

    @OnClick({R.id.tv_refund_detail, R.id.tv_store_name, R.id.iv_call, R.id.tv_left, R.id.tv_right})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.tv_refund_detail://退款详情
                startActivity(RefundDetailsActivity.newInstance(getCurContext(),
                        mRefundStatus, mRefundRemark, mPayType,mPrice));
                break;
            case R.id.tv_store_name://查看店铺
                intent = new Intent(getCurContext(), StoreDetailActivity.class);
                intent.putExtra("id", mStoreId);
                startActivity(intent);
                break;
            case R.id.iv_call://拨打电话
                XXPermissions.with(getCurActivity())
                        .permission(Permission.CALL_PHONE)
                        .request(new OnPermission() {

                            @Override
                            public void hasPermission(List<String> granted, boolean all) {
                                if (all) {
                                    new XPopup.Builder(getCurContext())
                                            .asConfirm(getResources().getString(R.string.Tips), getResources().getString(R.string.hint_confirm_contact),
                                                    getResources().getString(R.string.cancel), getResources().getString(R.string.confirm_short),
                                                    () -> {
                                                        if (mPhoneList.size() > 0) {
                                                            String[] strings = new String[mPhoneList.size()];
                                                            mPhoneList.toArray(strings);
                                                            new XPopup.Builder(getCurActivity())
                                                                    .isDarkTheme(false)
                                                                    .asBottomList(getResources().getString(R.string.business_phone), strings,
                                                                            (position, text) -> {
                                                                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                                                                Uri data = Uri.parse("tel:" +mPhoneList.get(position));
                                                                                callIntent.setData(data);
                                                                                startActivity(callIntent);
                                                                            })
                                                                    .show();
                                                        } else {
                                                        }
                                                    }, null, false)
                                            .show();
                                } else {
                                }
                            }

                            @Override
                            public void noPermission(List<String> denied, boolean quick) {
                            }
                        });
                break;
            case R.id.tv_left://底部左边按钮
                if (mTvLeft.getText().equals(getResources().getString(R.string.to_evaluate))) {
                    //去评价
                    intent = new Intent(getCurActivity(), OrderEvaluateActivity.class);
                    intent.putExtra("order_id", mOId);
                    intent.putExtra("store_id", mStoreId);
                    intent.putExtra("store_name", mStoreName);
                    intent.putExtra("store_logo", mStoreLogo);
                    intent.putExtra("goods_name", mGoodsName);
                    intent.putExtra("price", mPrice);
                    startActivity(intent);
                } else if (mTvLeft.getText().equals(getResources().getString(R.string.apply_for_refund))) {
                    //申请退款
                    intent = new Intent(getCurActivity(), ApplyForRefundActivity.class);
                    intent.putExtra("order_id", mOrderId);
                    intent.putExtra("store_id", mStoreId);
                    intent.putExtra("store_name", mStoreName);
                    intent.putExtra("store_logo", mStoreLogo);
                    intent.putExtra("goods_name", mGoodsName);
                    intent.putExtra("price", mPrice);
                    startActivity(intent);
                } else if (mTvLeft.getText().equals(getResources().getString(R.string.cancellation_of_order))) {
                    //取消订单
                    new XPopup.Builder(getCurContext())
                            .asConfirm(getResources().getString(R.string.Tips), getResources().getString(R.string.cancel_order_tip),
                                    getResources().getString(R.string.cancel), getResources().getString(R.string.confirm_short),
                                    () -> mPresenter.cancelOrder(mOrderId), null, false)
                            .show();
                } else if (mTvLeft.getText().equals(getResources().getString(R.string.view_progress))) {
                    startActivity(RefundDetailsActivity.newInstance(getCurContext(),
                            mRefundStatus, mRefundRemark, mPayType,mPrice));
                }
                break;
            case R.id.tv_right://底部右边按钮
                if (mTvRight.getText().equals(getResources().getString(R.string.to_evaluate))) {
                    //去评价
                    intent = new Intent(getCurActivity(), OrderEvaluateActivity.class);
                    intent.putExtra("order_id", mOId);
                    intent.putExtra("store_id", mStoreId);
                    intent.putExtra("store_name", mStoreName);
                    intent.putExtra("store_logo", mStoreLogo);
                    intent.putExtra("goods_name", mGoodsName);
                    intent.putExtra("price", mPrice);
                    startActivity(intent);
                } else if (mTvRight.getText().equals(getResources().getString(R.string.order_to_pay))) {
                    //去付款
                    intent = new Intent(getCurActivity(), OrderPayActivity.class);
                    intent.putExtra("order_id", mOrderId);
                    intent.putExtra("price", mPrice);
                    startActivity(intent);
                } else if (mTvRight.getText().equals(getResources().getString(R.string.confirm_receipt))) {
                    //确认收货
                    new XPopup.Builder(getCurContext())
                            .asConfirm(getResources().getString(R.string.Tips), getResources().getString(R.string.confirm_receipt_tip),
                                    getResources().getString(R.string.cancel), getResources().getString(R.string.confirm_short),
                                    () -> mPresenter.confirmOrder(mOrderId), null, false)
                            .show();
                } else if (mTvRight.getText().equals(getResources().getString(R.string.another_one))) {
                    //再来一单
                    mPresenter.reBuy(mOrderId, mStoreId);
                } else if (mTvRight.getText().equals(getResources().getString(R.string.apply_for_refund))) {
                    //申请退款
                    intent = new Intent(getCurActivity(), ApplyForRefundActivity.class);
                    intent.putExtra("order_id", mOrderId);
                    intent.putExtra("store_id", mStoreId);
                    intent.putExtra("store_name", mStoreName);
                    intent.putExtra("store_logo", mStoreLogo);
                    intent.putExtra("goods_name", mGoodsName);
                    intent.putExtra("price", mPrice);
                    startActivity(intent);
                } else if (mTvRight.getText().equals(getResources().getString(R.string.cancellation_of_order))) {
                    //取消订单
                    new XPopup.Builder(getCurContext())
                            .asConfirm(getResources().getString(R.string.Tips), getResources().getString(R.string.cancel_order_tip),
                                    getResources().getString(R.string.cancel), getResources().getString(R.string.confirm_short),
                                    () -> mPresenter.cancelOrder(mOrderId), null, false)
                            .show();
                } else if (mTvRight.getText().equals(getResources().getString(R.string.cancel_refund))) {
                    //取消退款
                    new XPopup.Builder(getCurContext())
                            .asConfirm(getResources().getString(R.string.Tips), getResources().getString(R.string.cancel_refund_tip),
                                    getResources().getString(R.string.cancel), getResources().getString(R.string.confirm_short),
                                    () -> mPresenter.cancelRefund(mOrderId), null, false)
                            .show();
                }
                break;
        }
    }

    @Override
    public void getOrderDetailSuccess(OrderDetailBean data) {
        mShareTitle = data.getShareInfo().getTitle();
        mShareDesc = data.getShareInfo().getContent();
        mOrderId = data.getOrderInfo().getOrderId();
        mStoreName = data.getOrderInfo().getShopName();
        mStoreLogo = data.getOrderInfo().getShopLogo();
        if (data.getOrderInfo().getGoodsCount() > 1) {
            mGoodsName = getResources().getString(R.string.order_goods_name,
                    data.getOrderInfo().getGoodsList().get(0).getGName(),
                    String.valueOf(data.getOrderInfo().getGoodsCount()));
        } else {
            mGoodsName = data.getOrderInfo().getGoodsList().get(0).getGName();
        }
        mOrderStatus = data.getOrderInfo().getOrderStatus();
        mRefundStatus = data.getOrderInfo().getRefundStatus();
        mRefundRemark = data.getOrderInfo().getRefundRemark();
        mPrice = data.getOrderInfo().getTotalPrice();
        //根据订单状态显示不同进度
        //判断是不是未付款订单
        //支付状态:1:成功,2:等待付款,3:取消
        if ("2".equals(data.getOrderInfo().getPayStatus())) {
            if ("6".equals(data.getOrderInfo().getOrderStatus())) {
                mTvLeft.setVisibility(View.INVISIBLE);
                mTvRight.setText(getResources().getString(R.string.another_one));

                //隐藏顶部状态
                mClOrderStatus.setVisibility(View.GONE);
            } else {
                mIvCheckoutSuccess.setImageResource(R.drawable.circle_gray_bg);
                mTvCheckoutSuccess.setTextColor(ContextCompat.getColor(getCurContext(), R.color.gray_text));
                mVStatusWaitingForOrder.setBackgroundColor(ContextCompat.getColor(getCurContext(), R.color.gray_text));
                mIvWaitingForOrder.setImageResource(R.drawable.circle_gray_bg);
                mTvWaitingForOrder.setTextColor(ContextCompat.getColor(getCurContext(), R.color.gray_text));
            }
        } else if ("3".equals(data.getOrderInfo().getPayStatus())) {
            mTvLeft.setVisibility(View.INVISIBLE);
            mTvRight.setText(getResources().getString(R.string.another_one));
            mIvCheckoutSuccess.setImageResource(R.drawable.circle_gray_bg);
            mTvCheckoutSuccess.setTextColor(ContextCompat.getColor(getCurContext(), R.color.gray_text));
            mVStatusWaitingForOrder.setBackgroundColor(ContextCompat.getColor(getCurContext(), R.color.gray_text));
            mIvWaitingForOrder.setImageResource(R.drawable.circle_gray_bg);
            mTvWaitingForOrder.setTextColor(ContextCompat.getColor(getCurContext(), R.color.gray_text));
            //隐藏顶部状态
            mClOrderStatus.setVisibility(View.GONE);
        } else {
            //判断退款状态
            //退款状态1:同意，2:等待退款,3:拒绝
            if ("1".equals(data.getOrderInfo().getRefundStatus())) {
                mRlRefund.setVisibility(View.VISIBLE);
                mTvRefundStatus.setText(getResources().getString(R.string.merchant_agrees_to_refund));
                mVBottomLine.setVisibility(View.GONE);
                mLlBottom.setVisibility(View.GONE);

                //隐藏顶部状态
                mClOrderStatus.setVisibility(View.GONE);
            } else if ("2".equals(data.getOrderInfo().getRefundStatus())) {
                mRlRefund.setVisibility(View.VISIBLE);
                mTvRefundStatus.setText(getResources().getString(R.string.waiting_for_the_merchant_to_process));
                mTvLeft.setVisibility(View.INVISIBLE);
                mTvRight.setText(getResources().getString(R.string.cancel_refund));
                //隐藏顶部状态
                mClOrderStatus.setVisibility(View.GONE);
            } else if ("3".equals(data.getOrderInfo().getRefundStatus())) {
                mRlRefund.setVisibility(View.VISIBLE);
                mTvRefundStatus.setText(getResources().getString(R.string.merchant_rejects_refund));
                mTvRight.setText(getResources().getString(R.string.apply_for_refund));
                mTvRight.setText(getResources().getString(R.string.confirm_receipt));

                //订单状态:1:成功,2:等待接单,3:备餐中,4:配送中/通知取餐,5:退款完成,6:用户取消'
                if (data.getOrderInfo().getOrderStatus().equals("2")) {
                    mTvLeft.setVisibility(View.INVISIBLE);
                    mTvRight.setText(getResources().getString(R.string.cancellation_of_order));
                } else if (mOrderStatus.equals("3")) {
                    mTvWaitingForOrder.setText(getResources().getString(R.string.received_order));
                    mTvLeft.setText(getResources().getString(R.string.apply_for_refund));
                    mTvRight.setText(getResources().getString(R.string.confirm_receipt));
                } else if (mOrderStatus.equals("4")) {
                    mTvLeft.setText(getResources().getString(R.string.apply_for_refund));
                    mTvRight.setText(getResources().getString(R.string.confirm_receipt));
                    mTvWaitingForOrder.setText(getResources().getString(R.string.received_order));
                    mVStatusDistribution.setBackgroundColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                    mTvDistributionInProgress.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                    mIvDistributionInProgress.setImageResource(R.drawable.circle_red_bg);
                } else if (mOrderStatus.equals("5")) {
                    mTvLeft.setVisibility(View.INVISIBLE);
                    mTvRight.setText(getResources().getString(R.string.another_one));
                    //隐藏顶部状态
                    mClOrderStatus.setVisibility(View.GONE);
                } else if (mOrderStatus.equals("6")) {
                    mVBottomLine.setVisibility(View.GONE);
                    mLlBottom.setVisibility(View.GONE);
                    //隐藏顶部状态
                    mClOrderStatus.setVisibility(View.GONE);
                } else {
                    if ("0".equals(data.getOrderInfo().getEvaluateStatus())) {
                        mTvLeft.setText(getResources().getString(R.string.to_evaluate));
                    } else {
                        mTvLeft.setVisibility(View.INVISIBLE);
                    }
                    mTvRight.setText(getResources().getString(R.string.another_one));
                    mTvWaitingForOrder.setText(getResources().getString(R.string.received_order));
                    mVStatusDistribution.setBackgroundColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                    mTvDistributionInProgress.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                    mIvDistributionInProgress.setImageResource(R.drawable.circle_red_bg);
                    mVStatusComplete.setBackgroundColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                    mTvComplete.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                    mIvComplete.setImageResource(R.drawable.circle_red_bg);
                }
            } else {//订单不是退款状态
                //设置订单状态 tv_order_status
                //订单状态:1:成功,2:等待接单,3:备餐中,4:配送中/通知取餐,5:退款完成,6:用户取消'
                if (data.getOrderInfo().getOrderStatus().equals("2")) {
                    mTvLeft.setVisibility(View.INVISIBLE);
                    mTvRight.setText(getResources().getString(R.string.cancellation_of_order));
                } else if (mOrderStatus.equals("3")) {
                    mTvWaitingForOrder.setText(getResources().getString(R.string.received_order));
                    if (!TextUtils.isEmpty(data.getOrderInfo().getReservedTelephone())) {
                        mTvDistributionInProgress.setText(getResources().getString(R.string.have_eaten));
                    }
                    mTvLeft.setText(getResources().getString(R.string.apply_for_refund));
                    mTvRight.setText(getResources().getString(R.string.confirm_receipt));
                } else if (mOrderStatus.equals("4")) {
                    mTvLeft.setText(getResources().getString(R.string.apply_for_refund));
                    mTvRight.setText(getResources().getString(R.string.confirm_receipt));
                    mTvWaitingForOrder.setText(getResources().getString(R.string.received_order));
                    mVStatusDistribution.setBackgroundColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                    mTvDistributionInProgress.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                    mIvDistributionInProgress.setImageResource(R.drawable.circle_red_bg);
                } else if (mOrderStatus.equals("5")) {
                    mTvLeft.setVisibility(View.INVISIBLE);
                    mTvRight.setText(getResources().getString(R.string.another_one));
                    //隐藏顶部状态
                    mClOrderStatus.setVisibility(View.GONE);
                } else if (mOrderStatus.equals("6")) {
                    mVBottomLine.setVisibility(View.GONE);
                    mLlBottom.setVisibility(View.GONE);
                    //隐藏顶部状态
                    mClOrderStatus.setVisibility(View.GONE);
                } else {
                    ToolBarManager.with(this).setRightImage(R.mipmap.icon_red_envelopes, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showShareDialog();
                        }
                    });
                    if ("0".equals(data.getOrderInfo().getEvaluateStatus())) {
                        mTvLeft.setText(getResources().getString(R.string.to_evaluate));
                    } else {
                        mTvLeft.setVisibility(View.INVISIBLE);
                    }
                    mTvRight.setText(getResources().getString(R.string.another_one));
                    mTvWaitingForOrder.setText(getResources().getString(R.string.received_order));
                    mVStatusDistribution.setBackgroundColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                    mTvDistributionInProgress.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                    mIvDistributionInProgress.setImageResource(R.drawable.circle_red_bg);
                    mVStatusComplete.setBackgroundColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                    mTvComplete.setTextColor(ContextCompat.getColor(getCurContext(), R.color.colorAccent));
                    mIvComplete.setImageResource(R.drawable.circle_red_bg);
                }
            }
        }

        //设置店铺名
        mTvStoreName.setText(mStoreName);
        //店铺电话
        for (int i = 0; i < data.getOrderInfo().getTels().size(); i++) {
            mPhoneList.add(data.getOrderInfo().getTels().get(i).getTel());
        }
        //设置商品
        mAdapter.setData(data.getOrderInfo().getGoodsList());
        //设置包装费
        mTvPackingFee.setText(getResources().getString(R.string.goods_price, "0"));
        //设置配送费
        mTvDeliveryFee.setText(getResources().getString(R.string.goods_price,
                data.getOrderInfo().getDeliveryPrice()));
        //设置店铺满减
        if ("0".equals(data.getOrderInfo().getFullReduction())) {
            mTvShopFullReductionText.setVisibility(View.GONE);
            mTvShopFullReduction.setVisibility(View.GONE);
        } else {
            mTvShopFullReduction.setText(getResources().getString(R.string.red_envelopes_value,
                    data.getOrderInfo().getFullReduction()));
        }
        if ("0".equals(data.getOrderInfo().getFirstReduction())) {
            mTvFirstReductionText.setVisibility(View.GONE);
            mTvFirstReduction.setVisibility(View.GONE);
        } else {
            //设置首单立减
            mTvFirstReduction.setText(getResources().getString(R.string.red_envelopes_value,
                    data.getOrderInfo().getFirstReduction()));
        }
        //设置红包金额
        //设置红包金额
        if ("0".equals(data.getOrderInfo().getRedRrice())) {
            mTvRedEnvelopes.setVisibility(View.GONE);
            mTvRedEnvelopesText.setVisibility(View.GONE);
        } else {
            mTvRedEnvelopes.setText(getResources().getString(R.string.red_envelopes_value,
                    data.getOrderInfo().getRedRrice()));
        }
        //设置优惠券金额
        mTvCoupon.setText(getResources().getString(R.string.goods_price, "0"));
        //合计
        mTvTotalPrice.setText(getResources().getString(R.string.goods_price,
                data.getOrderInfo().getTotalPrice()));
        if (TextUtils.isEmpty(data.getOrderInfo().getReservedTelephone())) {//外卖
            //设置收货信息
            if (!TextUtils.isEmpty(data.getOrderInfo().getAddress().getAddress())) {
                mTvReceivingInformation.setText(data.getOrderInfo().getAddress().getName().concat(" ")
                        .concat(data.getOrderInfo().getAddress().getPhone()).concat("\n")
                        .concat(data.getOrderInfo().getAddress().getAddress())
                        .concat(data.getOrderInfo().getAddress().getRoomNo()));
            }
            //设置期望送达时间
            mTvExpectedTime.setText(data.getOrderInfo().getExpectedDeliveryTime());
            //设置配送服务
            mTvDistributionService.setText(getResources().getString(R.string.merchant_distribution));
        } else {//自提
            mTvDistributionInformation.setText(getResources().getString(R.string.self_information));
            mTvReceivingInformationText.setText(getResources().getString(R.string.merchant_address));
            mTvReceivingInformation.setText(data.getOrderInfo().getShopAddress());
            mTvExpectedTimeText.setText(getResources().getString(R.string.self_extracting_time));
            mTvExpectedTime.setText(data.getOrderInfo().getExpectedDeliveryTime());
            mTvDistributionServiceText.setText(getResources().getString(R.string.reserved_telephone));
            mTvDistributionService.setText(data.getOrderInfo().getReservedTelephone());
        }
        //设置订单号码
        mTvOrderNumber.setText(data.getOrderInfo().getOrderId());
        //设置下单时间
        mTvOrderTime.setText(data.getOrderInfo().getCreateTime());
        //设置付款方式
        mPayType=data.getOrderInfo().getPayType();
        if (!TextUtils.isEmpty(mPayType)) {
            mTvPayType.setText(mPayType);
        }
    }

    @Override
    public void confirmOrderSuccess() {
        mPresenter.getOrderDetail(mOId);
    }

    @Override
    public void cancelOrderSuccess() {
        finish();
    }

    @Override
    public void cancelRefundSuccess() {
        finish();
    }

    @Override
    public void reBuySuccess(String storeId) {
        Intent intent = new Intent(getCurContext(), StoreDetailActivity.class);
        intent.putExtra("id", storeId);
        startActivity(intent);
    }

    @Override
    protected OrderDetailPresenter createPresenter() {
        return new OrderDetailPresenter(this);
    }

    private void showShareDialog() {
        UserInfoModel userInfo = UserCenter.getUserInfo();
        String code = userInfo.getInviteCode();
        String imageUrl = "";
        String webview = Constant.RED_ENVELOPE_URL.concat(mOId).concat("&iv=").concat(code).concat("&l=").concat(RepositoryFactory.getLocalRepository().getLangugeType());

        ShareDialog2 shareDialog = new ShareDialog2(getCurContext());
        shareDialog.setShareParams(mShareTitle, mShareDesc, imageUrl, webview);
        shareDialog.setShareType(ShareDialog2.ShareType.web);
        new XPopup.Builder(getCurContext())
                .asCustom(shareDialog)
                .show();
    }
}
