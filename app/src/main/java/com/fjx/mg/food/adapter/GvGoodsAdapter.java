package com.fjx.mg.food.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.view.RoundImageView;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.DimensionUtil;
import com.library.repository.models.HomeShopListBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yedeman
 * @date 2020/6/5.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class GvGoodsAdapter extends BaseAdapter {

    private Context mContext;
    private List<HomeShopListBean.ShopListBean.GoodsListBean> mList;

    public GvGoodsAdapter(Context context, List<HomeShopListBean.ShopListBean.GoodsListBean> list) {
        mContext = context;
        mList = list;
    }

    public void setData(List<HomeShopListBean.ShopListBean.GoodsListBean> list) {
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_gv_goods, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置商品图片
        holder.mIvPic.setRectAdius(DimensionUtil.dip2px(5));
        CommonImageLoader.load(mList.get(position).getgImg()).error(R.drawable.big_image_default)
                .placeholder(R.drawable.big_image_default).into(holder.mIvPic);
        //设置商品名
        holder.mTvGoodsName.setText(mList.get(position).getgName());
        //设置价格
        holder.mTvGoodsPrice.setText(mList.get(position).getPrice());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_pic)
        RoundImageView mIvPic;
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_goods_price)
        TextView mTvGoodsPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
