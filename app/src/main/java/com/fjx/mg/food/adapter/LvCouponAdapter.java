package com.fjx.mg.food.adapter;

import android.content.Context;
import android.text.Html;
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
public class LvCouponAdapter extends BaseAdapter {

    private Context mContext;
    private List<CouponBean.CouponListBean> mList;
    private int mSelectPos=-1;

    public LvCouponAdapter(Context context, List<CouponBean.CouponListBean> list) {
        mContext = context;
        mList = list;
    }

    public void setData(List<CouponBean.CouponListBean> list){
        mList = list;
        notifyDataSetChanged();
    }

    public void setSelectPos(int pos){
        this.mSelectPos=pos;
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
            convertView = View.inflate(mContext, R.layout.item_lv_coupon, null);
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
        //状态,1:已经使用,2:等待使用,3:已失效
        if (mList.get(position).isIsCanUse()){
            holder.mVLine.setVisibility(View.INVISIBLE);
            holder.mTvReason.setVisibility(View.GONE);
            holder.mIvSelected.setVisibility(View.VISIBLE);
            if (position==mSelectPos){
                holder.mIvSelected.setImageResource(R.mipmap.icon_selected);
            }else {
                holder.mIvSelected.setImageResource(R.mipmap.icon_unselected);
            }
            holder.mTvAmount.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
            holder.mTvUnit.setTextColor(ContextCompat.getColor(mContext,R.color.colorAccent));
            holder.mTvTitle.setTextColor(ContextCompat.getColor(mContext,R.color.black));
        }else {
            holder.mVLine.setVisibility(View.VISIBLE);
            holder.mTvReason.setVisibility(View.VISIBLE);
            holder.mIvSelected.setVisibility(View.INVISIBLE);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                holder.mTvReason.setText(Html.fromHtml(mContext.getResources().getString(R.string.reason_for_unavailability,
                        mList.get(position).getRemark()),Html.FROM_HTML_MODE_LEGACY));
            } else {
                holder.mTvReason.setText(Html.fromHtml(mContext.getResources().getString(R.string.reason_for_unavailability,
                        mList.get(position).getRemark())));
            }

            holder.mTvAmount.setTextColor(ContextCompat.getColor(mContext,R.color.gray_text));
            holder.mTvUnit.setTextColor(ContextCompat.getColor(mContext,R.color.gray_text));
            holder.mTvTitle.setTextColor(ContextCompat.getColor(mContext,R.color.gray_text));
        }
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
        @BindView(R.id.iv_selected)
        ImageView mIvSelected;
        @BindView(R.id.v_line)
        View mVLine;
        @BindView(R.id.tv_reason)
        TextView mTvReason;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
