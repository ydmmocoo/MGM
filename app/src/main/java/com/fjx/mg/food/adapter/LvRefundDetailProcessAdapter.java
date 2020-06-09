package com.fjx.mg.food.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.fjx.mg.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yedeman
 * @date 2020/5/19.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class LvRefundDetailProcessAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mList;
    private int mCurrentProcessPos = 0;

    public LvRefundDetailProcessAdapter(Context context) {
        mContext = context;
        mList = new String[]{mContext.getString(R.string.waiting_for_merchants_to_pass),
                mContext.getString(R.string.merchant_passed),
                mContext.getString(R.string.refunding),
                mContext.getString(R.string.refund_successful)};
    }

    public void setCurrentProcess(int pos) {
        mCurrentProcessPos = pos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.length;
    }

    @Override
    public Object getItem(int position) {
        return mList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_lv_refund_process, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
            holder.mVLine.setVisibility(View.GONE);
        } else {
            holder.mVLine.setVisibility(View.VISIBLE);
        }
        holder.mTvProcessName.setText(mList[position]);
        if (mCurrentProcessPos == position) {
            holder.mIvDot.setImageResource(R.drawable.dot_yellow);
            holder.mTvProcessName.setTextColor(ContextCompat.getColor(mContext, R.color.textColorYellow5));
        } else {
            holder.mIvDot.setImageResource(R.drawable.dot_gray);
            holder.mTvProcessName.setTextColor(ContextCompat.getColor(mContext, R.color.gray_text));
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.tv_process_name)
        TextView mTvProcessName;
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.iv_dot)
        ImageView mIvDot;
        @BindView(R.id.v_line)
        View mVLine;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
