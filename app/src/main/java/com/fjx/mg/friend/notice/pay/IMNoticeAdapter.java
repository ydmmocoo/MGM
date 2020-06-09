package com.fjx.mg.friend.notice.pay;

import android.text.TextUtils;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.common.base.adapter.BaseAdapter;
import com.library.repository.models.IMNoticeModel;

public class IMNoticeAdapter extends BaseAdapter<IMNoticeModel> {

    public IMNoticeAdapter() {
        super(R.layout.item_im_notice);
    }

    @Override
    protected void convert(BaseViewHolder helper, IMNoticeModel item) {
        helper.setText(R.id.tvTitle, item.getTitle());
        helper.setText(R.id.tvPriceAl, item.getPrice().concat("AR"));
        helper.setText(R.id.tvPrice, "¥".concat(item.getCnyPrice()));
        helper.setText(R.id.tvDate, item.getCreateTime());
        helper.setText(R.id.tvDate, item.getCreateTime());

        helper.setText(R.id.tvPayType, item.getPayType());

        if (TextUtils.equals("14", item.getType())) {
            helper.setText(R.id.tvHint1, R.string.refund_amount);
            //退款
            if (TextUtils.isEmpty(item.getRemark())) {
                //显示退款方
                helper.setText(R.id.tvHint2, R.string.refund_party);
                helper.setText(R.id.tvToPerson, item.getFrom());
            } else {
                //显示退还原因
                helper.setText(R.id.tvToPerson, item.getRemark());
                helper.setText(R.id.tvHint2, R.string.refund_reasons);
            }
            helper.setText(R.id.tvHint3, R.string.refund_type);
        } else if (TextUtils.equals("23", item.getType())) {
            helper.setText(R.id.tvHint2, getHintText(item.getType()));
            helper.setText(R.id.tvToPerson, item.getTo());
        } else if (TextUtils.equals("9", item.getType()) || TextUtils.equals("34", item.getType())
                || TextUtils.equals("10", item.getType()) || TextUtils.equals("28", item.getType())
                || TextUtils.equals("24", item.getType())) {
            helper.setText(R.id.tvHint2, getHintText(item.getType()));
            helper.setText(R.id.tvToPerson, item.getFrom());
        } else if (TextUtils.equals("30", item.getType())) {
            helper.setText(R.id.tvHint2, R.string.remark);
            helper.setText(R.id.tvToPerson, item.getRemark());
            helper.setText(R.id.tvHint1, R.string.get_money);
            helper.setText(R.id.tvHint3, R.string.get_mode);
        } else if (TextUtils.equals("27", item.getType())) {
            helper.setText(R.id.tvHint2, R.string.recharge_network);
            helper.setText(R.id.tvToPerson, item.getRemark());
            helper.setText(R.id.tvHint1, R.string.withdraw_account);
            helper.setText(R.id.tvHint3, R.string.pay_mode);
        } else if (TextUtils.equals("33", item.getType())) {
            helper.setText(R.id.tvHint2, R.string.sales_network);
            helper.setText(R.id.tvToPerson, item.getFrom());
            helper.setText(R.id.tvHint1, R.string.transfer_account);
            helper.setText(R.id.tvHint3, R.string.pay_mode);
        } else if (TextUtils.equals("7", item.getType()) || TextUtils.equals("8", item.getType())
                || TextUtils.equals("35", item.getType()) || TextUtils.equals("23", item.getType())
                || TextUtils.equals("31", item.getType())) {
            helper.setText(R.id.tvToPerson, item.getTo());
            helper.setText(R.id.tvHint2, getHintText(item.getType()));
        } else {
            helper.setText(R.id.tvHint1, R.string.refund_amound);
            //支付转账等
            helper.setText(R.id.tvHint2, getHintText(item.getType()));
            helper.setText(R.id.tvHint3, R.string.pay_type);
            if (TextUtils.isEmpty(getPayText(item.getType()))) {
                //显示缴费类型
                helper.setText(R.id.tvToPerson, item.getTo());
            } else {
                //显示收款方
                helper.setText(R.id.tvToPerson, item.getRemark());
            }

        }

    }

    private String getHintText(String type) {
        switch (type) {
            case "1"://充值
            case "2"://话费
            case "3"://流量
            case "4"://电费
            case "5"://水费
            case "6"://网费
                return getContext().getString(R.string.payment_content);
            case "9"://收款
            case "10"://收红包
            case "28"://同城置顶
            case "24"://二维码收款
            case "34"://发群聊红包
                return getContext().getString(R.string.payer);
            case "7"://转账
            case "8"://红包
            case "11"://外卖
            case "12"://机票
            case "13"://酒店
            case "35"://发群聊红包
            case "23"://二维码付款
            case "31"://店铺扫码
                return getContext().getString(R.string.receiver);
        }
        return "";
    }

    private String getPayText(String type) {
        switch (type) {
            case "1"://充值
                return getContext().getString(R.string.recharge);
            case "2"://话费
                return getContext().getString(R.string.telephone_bill);
            case "3"://流量
                return getContext().getString(R.string.flow);
            case "4"://电费
                return getContext().getString(R.string.eectricity_bill);
            case "5"://水费
                return getContext().getString(R.string.water_bill);
            case "6"://网费
                return getContext().getString(R.string.net_bill);
            case "7"://转账
            case "8"://红包
            case "9"://收款
            case "10"://收红包
            case "11"://外卖
            case "12"://机票
            case "13"://酒店
            case "35"://收群聊红包
                return "";
        }
        return "";
    }


}
