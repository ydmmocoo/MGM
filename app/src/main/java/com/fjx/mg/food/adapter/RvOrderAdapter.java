package com.fjx.mg.food.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.food.ApplyForRefundActivity;
import com.fjx.mg.food.OrderEvaluateActivity;
import com.fjx.mg.food.OrderPayActivity;
import com.fjx.mg.food.RefundDetailsActivity;
import com.fjx.mg.view.RoundImageView;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.DimensionUtil;
import com.library.repository.models.OrderBean;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/28.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class RvOrderAdapter extends BaseQuickAdapter<OrderBean.OrderListBean, BaseViewHolder> {

    private OnBtnClickListener mListener;

    public RvOrderAdapter(int layoutResId, @Nullable List<OrderBean.OrderListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, OrderBean.OrderListBean item) {
        //设置店铺名 tv_store_name
        helper.setText(R.id.tv_store_name, item.getShopName());
        //设置日期 tv_date
        helper.setText(R.id.tv_date, item.getCreateTime());
        //判断是不是未付款订单
        //支付状态:1:成功,2:等待付款,3:取消
        if ("2".equals(item.getPayStatus())) {
            helper.setTextColor(R.id.tv_order_status, ContextCompat.getColor(getContext(), R.color.colorAccent));
            helper.setText(R.id.tv_order_status, getContext().getResources().getString(R.string.to_be_paid));
            helper.setVisible(R.id.btn_left, true);
            helper.setVisible(R.id.btn_right, true);
            helper.setText(R.id.btn_left, getContext().getResources().getString(R.string.cancellation_of_order));
            helper.setBackgroundResource(R.id.btn_right, R.drawable.btn_red_bg);
            helper.setTextColor(R.id.btn_right, ContextCompat.getColor(getContext(), R.color.colorAccent));
            helper.setText(R.id.btn_right, getContext().getResources().getString(R.string.order_to_pay));
        } else if ("3".equals(item.getPayStatus())) {
            helper.setTextColor(R.id.tv_order_status, ContextCompat.getColor(getContext(), R.color.gray_text));
            helper.setText(R.id.tv_order_status, getContext().getResources().getString(R.string.user_cancel));
            helper.setVisible(R.id.btn_left, false);
            helper.setVisible(R.id.btn_right, true);
            helper.setBackgroundResource(R.id.btn_right, R.drawable.btn_gray_bg);
            helper.setTextColor(R.id.btn_right, ContextCompat.getColor(getContext(), R.color.black));
            helper.setText(R.id.btn_right, getContext().getResources().getString(R.string.another_one));
        } else {
            //判断退款状态
            //退款状态1:同意，2:等待退款,3:拒绝
            if ("1".equals(item.getRefundStatus())) {
                helper.setTextColor(R.id.tv_order_status, ContextCompat.getColor(getContext(), R.color.gray_text));
                helper.setText(R.id.tv_order_status, getContext().getResources().getString(R.string.merchant_agrees_to_refund));
                helper.setVisible(R.id.btn_left, false);
                helper.setVisible(R.id.btn_right, true);
                helper.setBackgroundResource(R.id.btn_right, R.drawable.btn_gray_bg);
                helper.setTextColor(R.id.btn_right, ContextCompat.getColor(getContext(), R.color.black));
                helper.setText(R.id.btn_right, getContext().getResources().getString(R.string.refund_detail));
            } else if ("2".equals(item.getRefundStatus())) {
                helper.setTextColor(R.id.tv_order_status, ContextCompat.getColor(getContext(), R.color.gray_text));
                helper.setText(R.id.tv_order_status, getContext().getResources().getString(R.string.refund_processing));
                helper.setVisible(R.id.btn_left, true);
                helper.setVisible(R.id.btn_right, true);
                helper.setText(R.id.btn_left, getContext().getResources().getString(R.string.view_progress));
                helper.setBackgroundResource(R.id.btn_right, R.drawable.btn_gray_bg);
                helper.setTextColor(R.id.btn_right, ContextCompat.getColor(getContext(), R.color.black));
                helper.setText(R.id.btn_right, getContext().getResources().getString(R.string.cancel_refund));
            } else if ("3".equals(item.getRefundStatus())) {
                helper.setTextColor(R.id.tv_order_status, ContextCompat.getColor(getContext(), R.color.gray_text));
                helper.setText(R.id.tv_order_status, getContext().getResources().getString(R.string.merchant_rejects_refund));
                helper.setVisible(R.id.btn_left, true);
                helper.setVisible(R.id.btn_right, true);
                helper.setText(R.id.btn_left, getContext().getResources().getString(R.string.apply_for_refund));
                helper.setBackgroundResource(R.id.btn_right, R.drawable.btn_red_bg);
                helper.setTextColor(R.id.btn_right, ContextCompat.getColor(getContext(), R.color.black));
                helper.setText(R.id.btn_right, getContext().getResources().getString(R.string.confirm_receipt));
            } else {//订单不是退款状态
                //设置订单状态 tv_order_status
                //订单状态:1:成功,2:等待接单,3:备餐中,4:配送中/通知取餐,5:退款完成,6:用户取消'
                if (item.getOrderStatus().equals("2")) {
                    helper.setTextColor(R.id.tv_order_status, ContextCompat.getColor(getContext(), R.color.textColorYellow5));
                    helper.setText(R.id.tv_order_status, getContext().getResources().getString(R.string.waiting_for_order));
                    helper.setVisible(R.id.btn_left, false);
                    helper.setVisible(R.id.btn_right, true);
                    helper.setBackgroundResource(R.id.btn_right, R.drawable.btn_gray_bg);
                    helper.setTextColor(R.id.btn_right, ContextCompat.getColor(getContext(), R.color.black));
                    helper.setText(R.id.btn_right, getContext().getResources().getString(R.string.cancellation_of_order));
                } else if (item.getOrderStatus().equals("3")) {
                    helper.setTextColor(R.id.tv_order_status, ContextCompat.getColor(getContext(), R.color.colorGreen));
                    helper.setText(R.id.tv_order_status, getContext().getResources().getString(R.string.in_preparation));
                    helper.setVisible(R.id.btn_left, false);
                    helper.setVisible(R.id.btn_right, true);
                    helper.setBackgroundResource(R.id.btn_right, R.drawable.btn_gray_bg);
                    helper.setTextColor(R.id.btn_right, ContextCompat.getColor(getContext(), R.color.black));
                    helper.setText(R.id.btn_right, getContext().getResources().getString(R.string.apply_for_refund));
                } else if (item.getOrderStatus().equals("4")) {
                    helper.setTextColor(R.id.tv_order_status, ContextCompat.getColor(getContext(), R.color.colorGreen));
                    helper.setText(R.id.tv_order_status, getContext().getResources().getString(R.string.distribution_in_progress));
                    helper.setVisible(R.id.btn_left, true);
                    helper.setVisible(R.id.btn_right, true);
                    helper.setBackgroundResource(R.id.btn_left, R.drawable.btn_gray_bg);
                    helper.setText(R.id.btn_left, getContext().getResources().getString(R.string.apply_for_refund));
                    helper.setBackgroundResource(R.id.btn_right, R.drawable.btn_gray_bg);
                    helper.setTextColor(R.id.btn_right, ContextCompat.getColor(getContext(), R.color.black));
                    helper.setText(R.id.btn_right, getContext().getResources().getString(R.string.confirm_receipt));
                } else if (item.getOrderStatus().equals("5")) {
                    helper.setTextColor(R.id.tv_order_status, ContextCompat.getColor(getContext(), R.color.gray_text));
                    helper.setText(R.id.tv_order_status, getContext().getResources().getString(R.string.refund_complete));
                    helper.setVisible(R.id.btn_left, false);
                    helper.setVisible(R.id.btn_right, true);
                    helper.setBackgroundResource(R.id.btn_right, R.drawable.btn_gray_bg);
                    helper.setTextColor(R.id.btn_right, ContextCompat.getColor(getContext(), R.color.black));
                    helper.setText(R.id.btn_right, getContext().getResources().getString(R.string.another_one));
                } else if (item.getOrderStatus().equals("6")) {
                    helper.setTextColor(R.id.tv_order_status, ContextCompat.getColor(getContext(), R.color.gray_text));
                    helper.setText(R.id.tv_order_status, getContext().getResources().getString(R.string.user_cancel));
                    helper.setVisible(R.id.btn_left, false);
                    helper.setVisible(R.id.btn_right, true);
                    helper.setBackgroundResource(R.id.btn_right, R.drawable.btn_gray_bg);
                    helper.setTextColor(R.id.btn_right, ContextCompat.getColor(getContext(), R.color.black));
                    helper.setText(R.id.btn_right, getContext().getResources().getString(R.string.another_one));
                } else {
                    helper.setTextColor(R.id.tv_order_status, ContextCompat.getColor(getContext(), R.color.gray_text));
                    helper.setText(R.id.tv_order_status, getContext().getResources().getString(R.string.order_completed));
                    helper.setVisible(R.id.btn_right, true);
                    if ("1".equals(item.getEvaluateStatus())) {
                        helper.setVisible(R.id.btn_left, false);
                    } else {
                        helper.setVisible(R.id.btn_left, true);
                        helper.setBackgroundResource(R.id.btn_left, R.drawable.btn_gray_bg);
                        helper.setText(R.id.btn_left, getContext().getResources().getString(R.string.to_evaluate));
                    }
                    helper.setBackgroundResource(R.id.btn_right, R.drawable.btn_gray_bg);
                    helper.setTextColor(R.id.btn_right, ContextCompat.getColor(getContext(), R.color.black));
                    helper.setText(R.id.btn_right, getContext().getResources().getString(R.string.another_one));
                }
            }
        }
        //设置商品图片 iv_goods_pic
        RoundImageView ivPic = helper.getView(R.id.iv_goods_pic);
        ivPic.setRectAdius(DimensionUtil.dip2px(5));
        CommonImageLoader.load(item.getGoodsList().get(0).getImg())
                .placeholder(R.drawable.food_default).into(ivPic);
        //设置商品名 tv_goods_name
        String goodsName;
        if (item.getGoodsCount() > 1) {
            goodsName = getContext().getResources().getString(R.string.order_goods_name,
                    item.getGoodsList().get(0).getGName(), String.valueOf(item.getGoodsCount()));
        } else {
            goodsName = item.getGoodsList().get(0).getGName();
        }
        helper.setText(R.id.tv_goods_name, goodsName);
        //设置有效期 tv_term_of_validity
        helper.setVisible(R.id.tv_term_of_validity, false);
        //设置总价 tv_total_price
        helper.setText(R.id.tv_total_price, getContext().getResources().getString(R.string.order_total_price,
                item.getTotalPrice()));
        //右边按钮点击事件 btn_right
        TextView btnRight = helper.getView(R.id.btn_right);
        btnRight.setOnClickListener(v -> {
            Intent intent = null;
            if (btnRight.getText().equals(getContext().getResources().getString(R.string.to_evaluate))) {
                //去评价
                intent = new Intent(getContext(), OrderEvaluateActivity.class);
                intent.putExtra("order_id", item.getOId());
                intent.putExtra("store_id", item.getSId());
                intent.putExtra("store_name", item.getShopName());
                intent.putExtra("store_logo", item.getShopLogo());
                intent.putExtra("goods_name", goodsName);
                intent.putExtra("price", item.getTotalPrice());
                getContext().startActivity(intent);
            } else if (btnRight.getText().equals(getContext().getResources().getString(R.string.order_to_pay))) {
                //去付款
                intent = new Intent(getContext(), OrderPayActivity.class);
                intent.putExtra("order_id", item.getOrderId());
                intent.putExtra("price", item.getTotalPrice());
                getContext().startActivity(intent);
            } else if (btnRight.getText().equals(getContext().getResources().getString(R.string.confirm_receipt))) {
                //确认收货
                new XPopup.Builder(getContext())
                        .asConfirm(getContext().getResources().getString(R.string.Tips), getContext().getResources().getString(R.string.confirm_receipt_tip),
                                getContext().getResources().getString(R.string.cancel), getContext().getResources().getString(R.string.confirm_short),
                                () -> mListener.confirmReceipt(item.getOrderId()), null, false)
                        .show();
            } else if (btnRight.getText().equals(getContext().getResources().getString(R.string.another_one))) {
                //再来一单
                mListener.addAnotherOne(item.getOId(), item.getSId());
            } else if (btnRight.getText().equals(getContext().getResources().getString(R.string.refund_detail))) {
                //退款详情
                intent = new Intent(getContext(), RefundDetailsActivity.class);
                intent.putExtra("refund_remark", item.getRefundRemark());
                intent.putExtra("refund_status", item.getRefundStatus());
                getContext().startActivity(intent);
            } else if (btnRight.getText().equals(getContext().getResources().getString(R.string.apply_for_refund))) {
                //申请退款
                intent = new Intent(getContext(), ApplyForRefundActivity.class);
                intent.putExtra("order_id", item.getOrderId());
                intent.putExtra("store_id", item.getSId());
                intent.putExtra("store_name", item.getShopName());
                intent.putExtra("store_logo", item.getShopLogo());
                intent.putExtra("goods_name", goodsName);
                intent.putExtra("price", item.getTotalPrice());
                getContext().startActivity(intent);
            } else if (btnRight.getText().equals(getContext().getResources().getString(R.string.cancellation_of_order))) {
                //取消订单
                new XPopup.Builder(getContext())
                        .asConfirm(getContext().getResources().getString(R.string.Tips), getContext().getResources().getString(R.string.cancel_order_tip),
                                getContext().getResources().getString(R.string.cancel), getContext().getResources().getString(R.string.confirm_short),
                                () -> mListener.cancelOrder(item.getOrderId()), null, false)
                        .show();
            } else if (btnRight.getText().equals(getContext().getResources().getString(R.string.cancel_refund))) {
                //取消退款
                new XPopup.Builder(getContext())
                        .asConfirm(getContext().getResources().getString(R.string.Tips), getContext().getResources().getString(R.string.cancel_refund_tip),
                                getContext().getResources().getString(R.string.cancel), getContext().getResources().getString(R.string.confirm_short),
                                () -> mListener.cancelRefund(item.getOrderId()), null, false)
                        .show();
            }
        });
        //左边按钮点击事件 btn_left
        TextView btnLeft = helper.getView(R.id.btn_left);
        btnLeft.setOnClickListener(v -> {
            Intent intent = null;
            if (btnLeft.getText().equals(getContext().getResources().getString(R.string.to_evaluate))) {
                //去评价
                intent = new Intent(getContext(), OrderEvaluateActivity.class);
                intent.putExtra("order_id", item.getOId());
                intent.putExtra("store_id", item.getSId());
                intent.putExtra("store_name", item.getShopName());
                intent.putExtra("store_logo", item.getShopLogo());
                intent.putExtra("goods_name", goodsName);
                intent.putExtra("price", item.getTotalPrice());
                getContext().startActivity(intent);
            } else if (btnLeft.getText().equals(getContext().getResources().getString(R.string.apply_for_refund))) {
                //申请退款
                intent = new Intent(getContext(), ApplyForRefundActivity.class);
                intent.putExtra("order_id", item.getOrderId());
                intent.putExtra("store_id", item.getSId());
                intent.putExtra("store_name", item.getShopName());
                intent.putExtra("store_logo", item.getShopLogo());
                intent.putExtra("goods_name", goodsName);
                intent.putExtra("price", item.getTotalPrice());
                getContext().startActivity(intent);
            } else if (btnLeft.getText().equals(getContext().getResources().getString(R.string.cancellation_of_order))) {
                //取消订单
                new XPopup.Builder(getContext())
                        .asConfirm(getContext().getResources().getString(R.string.Tips), getContext().getResources().getString(R.string.cancel_order_tip),
                                getContext().getResources().getString(R.string.cancel), getContext().getResources().getString(R.string.confirm_short),
                                () -> mListener.cancelOrder(item.getOrderId()), null, false)
                        .show();
            } else if (btnLeft.getText().equals(getContext().getResources().getString(R.string.view_progress))) {
                getContext().startActivity(RefundDetailsActivity.newInstance(getContext(),
                        item.getRefundStatus(), item.getRefundRemark(), item.getTotalPrice()));
            }
        });
    }

    public void setOnBtnClickListener(OnBtnClickListener listener) {
        mListener = listener;
    }

    public interface OnBtnClickListener {

        void addAnotherOne(String orderId, String storeId);

        void confirmReceipt(String orderId);

        void cancelOrder(String orderId);

        void cancelRefund(String orderId);
    }
}
