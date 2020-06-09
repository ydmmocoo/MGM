package com.fjx.mg.food.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.fjx.mg.R;
import com.library.repository.models.CouponBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yedeman
 * @date 2020/5/19.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class LvRedEnvelopeAndCouponAdapter extends BaseAdapter {

    private Context mContext;
    private List<CouponBean.CouponListBean> mList;

    public LvRedEnvelopeAndCouponAdapter(Context context, List<CouponBean.CouponListBean> list) {
        mContext = context;
        mList = list;
    }

    public void setData(List<CouponBean.CouponListBean> list){
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_lv_red_envelope_and_coupon, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        //设置红包面值
        holder.mTvAmount.setText(mList.get(position).getPrice());
        //设置红包标题
        holder.mTvTitle.setText(mList.get(position).getTypeName());
        //设置有效期
        holder.mTvDate.setText(mContext.getResources().getString(R.string.term_of_validity,
                mList.get(position).getValidDate()));
        //设置使用条件
        holder.mTvCondition.setText(mContext.getResources().getString(R.string.coupon_condition,
                mList.get(position).getFullPrice()));
        //立即使用
        holder.mTvUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_amount)
        TextView mTvAmount;
        @BindView(R.id.tv_unit)
        TextView mTvUnit;
        @BindView(R.id.tv_condition)
        TextView mTvCondition;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.tv_used)
        TextView mTvUsed;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
