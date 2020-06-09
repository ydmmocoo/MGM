package com.fjx.mg.food.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fjx.mg.R;
import com.fjx.mg.dialog.AddShopCartDialog;
import com.fjx.mg.food.StoreDetailActivity;
import com.fjx.mg.view.RoundImageView;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.CommonToast;
import com.library.common.utils.DimensionUtil;
import com.library.common.view.stickylistheaders.StickyListHeadersAdapter;
import com.library.repository.models.StoreGoodsBean;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author yedeman
 * @date 2020/5/26.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class LvStoreGoodsAdapter extends BaseAdapter implements StickyListHeadersAdapter {

    private List<StoreGoodsBean.CateListBean.GoodsListBean> mList;
    private StoreDetailActivity mContext;

    private OnAddShoppingCartListener mListener;

    public LvStoreGoodsAdapter(StoreDetailActivity context, List<StoreGoodsBean.CateListBean.GoodsListBean> list) {
        this.mList = list;
        this.mContext = context;
    }

    public void setData(List<StoreGoodsBean.CateListBean.GoodsListBean> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder headerViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_store_goods_header, null);
            headerViewHolder = new HeaderViewHolder(convertView);
            convertView.setTag(headerViewHolder);
        } else {
            headerViewHolder = (HeaderViewHolder) convertView.getTag();
        }
        headerViewHolder.mTvTitle.setText(mList.get(position).getGroupName());
        //headerViewHolder.mTvDesc.setText(mList.get(position).getGroup_desc());
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        return mList.get(position).getGroupId();
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_store_goods, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //设置商品图片
        holder.mIvPic.setRectAdius(DimensionUtil.dip2px(5));
        CommonImageLoader.load(mList.get(position).getGImg())
                .placeholder(R.drawable.food_default).into(holder.mIvPic);
        //设置商品名称
        holder.mTvName.setText(mList.get(position).getGName());
        //设置商品价格
        holder.mTvPrice.setText(mContext.getResources().getString(R.string.goods_price,
                mList.get(position).getPrice()));
        //设置是否显示起
        if (mList.get(position).getSpecialCount()<=1) {
            holder.mTvPriceMinimum.setVisibility(View.INVISIBLE);
        }else {
            holder.mTvPriceMinimum.setVisibility(View.VISIBLE);
        }
        //设置月售
        holder.mTvMonthlySales.setText(mContext.getResources().getString(R.string.monthly_sales,
                String.valueOf(mList.get(position).getSaleCount())));
        //设置商品数量
        if (mList.get(position).getCount() > 0) {
            holder.mIvLess.setVisibility(View.VISIBLE);
            holder.mTvCount.setVisibility(View.VISIBLE);
            holder.mTvCount.setText(String.valueOf(mList.get(position).getCount()));
        } else {
            holder.mIvLess.setVisibility(View.INVISIBLE);
            holder.mTvCount.setVisibility(View.INVISIBLE);
        }

        //添加商品
        holder.mIvPlus.setOnClickListener(v -> {
            if (mList.get(position).getSpecialList().size() <=1
                    && mList.get(position).getAttrList().size() == 0) {
                mListener.plusOne(mList.get(position).getGId(), mList.get(position).getGName(), "", "",
                        "", "", mList.get(position).getPrice(), "1", mList.get(position).getGImg(),holder.mIvPlus);
            } else {
                AddShopCartDialog dialog = new AddShopCartDialog(mContext);
                dialog.setData(mList.get(position).getGName(), mList.get(position).getSpecialList(), mList.get(position).getAttrList());
                dialog.setOnSelectedListener((seId, seName, aIds, aNames, price) -> {
                    mListener.plusOne(mList.get(position).getGId(), mList.get(position).getGName(), seId, seName,
                            aIds, aNames, price, "1", mList.get(position).getGImg(),holder.mIvPlus);
                    dialog.dismiss();
                });
                dialog.show();
            }
        });
        //移除商品
        holder.mIvLess.setOnClickListener(v -> {
            boolean hasAttr=false;
            if (mList.get(position).getSpecialList().size() > 0 || mList.get(position).getAttrList().size() > 0) {
                if (mList.get(position).getCount() > 1) {
                    CommonToast.toast(mContext.getResources().getString(R.string.goods_less_tips));
                    return;
                }else {
                    hasAttr=true;
                }
            }
            mList.get(position).setCount(0);
            mListener.lessOne(mList.get(position).getGId(), mList.get(position).getGName(), "", "",
                    "", "", mList.get(position).getPrice(), "-1", mList.get(position).getGImg(),hasAttr);
        });
        return convertView;
    }

    public void setOnAddShoppingCartListener(OnAddShoppingCartListener listener) {
        mListener = listener;
    }

    public interface OnAddShoppingCartListener {

        void plusOne(String id, String gName, String seId, String seName,
                     String aIds, String aNames, String price, String num, String img,ImageView ivAdd);

        void lessOne(String id, String gName, String seId, String seName,
                     String aIds, String aNames, String price, String num, String img,boolean hasAttr);

    }

    static class HeaderViewHolder {
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_desc)
        TextView mTvDesc;

        HeaderViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder {
        @BindView(R.id.iv_pic)
        RoundImageView mIvPic;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_monthly_sales)
        TextView mTvMonthlySales;
        @BindView(R.id.tv_price)
        TextView mTvPrice;
        @BindView(R.id.tv_price_minimum)
        TextView mTvPriceMinimum;
        @BindView(R.id.iv_plus)
        ImageView mIvPlus;
        @BindView(R.id.tv_count)
        TextView mTvCount;
        @BindView(R.id.iv_less)
        ImageView mIvLess;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
