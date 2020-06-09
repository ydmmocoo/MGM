package com.fjx.mg.food.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.view.RoundImageView;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.DimensionUtil;
import com.library.repository.models.HotShopBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yedeman
 * @date 2020/5/18.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class GvSelectedGoodStoreAdapter extends BaseAdapter {

    private Context mContext;
    private List<HotShopBean.ShopListBean> mList;

    public GvSelectedGoodStoreAdapter(Context context, List<HotShopBean.ShopListBean> list) {
        mContext = context;
        mList = list;
    }

    public void setData(List<HotShopBean.ShopListBean> list){
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
            convertView = View.inflate(mContext, R.layout.item_gv_selected_good_store, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }

        holder.mTvName.setText(mList.get(position).getName());
        holder.mIvBg.setRectAdius(DimensionUtil.dip2px(10));
        CommonImageLoader.load(mList.get(position).getGoodImg())
                .placeholder(R.drawable.food_default).into(holder.mIvBg);
        holder.mIvLogo.setRectAdius(DimensionUtil.dip2px(5));
        CommonImageLoader.load(mList.get(position).getLogo())
                .placeholder(R.drawable.food_default).into(holder.mIvLogo);
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_bg)
        RoundImageView mIvBg;
        @BindView(R.id.iv_logo)
        RoundImageView mIvLogo;
        @BindView(R.id.tv_name)
        TextView mTvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
