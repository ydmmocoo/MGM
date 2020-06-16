package com.fjx.mg.food.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.view.RoundImageView;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.DimensionUtil;
import com.library.repository.models.OrderDetailBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yedeman
 * @date 2020/5/31.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class LvOrderDetailGoodsAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderDetailBean.OrderInfoBean.GoodsListBean> mList;

    public LvOrderDetailGoodsAdapter(Context context, List<OrderDetailBean.OrderInfoBean.GoodsListBean> list) {
        mContext = context;
        mList = list;
    }

    public void setData(List<OrderDetailBean.OrderInfoBean.GoodsListBean> list){
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList==null?0:mList.size();
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
        LvGoodsAdapter.ViewHolder holder=null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_lv_goods, null);
            holder=new LvGoodsAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (LvGoodsAdapter.ViewHolder) convertView.getTag();
        }
        //设置商品图片
        holder.mIvPic.setRectAdius(DimensionUtil.dip2px(5));
        CommonImageLoader.load(mList.get(position).getImg())
                .placeholder(R.drawable.food_default).into(holder.mIvPic);
        //设置商品名
        holder.mTvGoodsName.setText(mList.get(position).getGName());
        //设置价格
        holder.mTvPrice.setText(mContext.getResources().getString(R.string.goods_price,
                mList.get(position).getPrice()));
        //设置数量
        holder.mTvCount.setText("X"+mList.get(position).getNum());
        //设置属性规格
        if (!TextUtils.isEmpty(mList.get(position).getSeName())&&
                !TextUtils.isEmpty(mList.get(position).getAName())) {
            holder.mTvAttribute.setText(mList.get(position).getSeName().concat("+")
                    .concat(mList.get(position).getAName()));
        }else if (!TextUtils.isEmpty(mList.get(position).getSeName())&&
                TextUtils.isEmpty(mList.get(position).getAName())){
            holder.mTvAttribute.setText(mList.get(position).getSeName());
        }else if (TextUtils.isEmpty(mList.get(position).getSeName())&&
                !TextUtils.isEmpty(mList.get(position).getAName())){
            holder.mTvAttribute.setText(mList.get(position).getAName());
        }else {
            holder.mTvAttribute.setText("");
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.iv_pic)
        RoundImageView mIvPic;
        @BindView(R.id.tv_goods_name)
        TextView mTvGoodsName;
        @BindView(R.id.tv_price)
        TextView mTvPrice;
        @BindView(R.id.tv_attribute)
        TextView mTvAttribute;
        @BindView(R.id.tv_count)
        TextView mTvCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
