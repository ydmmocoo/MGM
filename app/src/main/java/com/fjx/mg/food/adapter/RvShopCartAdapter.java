package com.fjx.mg.food.adapter;

import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.library.repository.models.ShopingCartBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yedeman
 * @date 2020/5/28.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class RvShopCartAdapter extends BaseQuickAdapter<ShopingCartBean.GoodsListBean, BaseViewHolder> {

    private OnShoppingCartEditListener mListener;

    public RvShopCartAdapter(int layoutResId, @Nullable List<ShopingCartBean.GoodsListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, ShopingCartBean.GoodsListBean item) {
        //设置名字
        helper.setText(R.id.tv_name,item.getGName());
        //设置规格属性
        if (TextUtils.isEmpty(item.getANames())){
            helper.setText(R.id.tv_attribute, item.getSeName());
        }else {
            helper.setText(R.id.tv_attribute, item.getSeName() + "+" + item.getANames().replace(",", "+"));
        }
        //设置价格
        helper.setText(R.id.tv_price,getContext().getResources().getString(R.string.goods_price,item.getPirce()));
        //设置数量
        helper.setText(R.id.tv_count,item.getNum());
        //增加商品
        helper.getView(R.id.iv_plus).setOnClickListener(v -> mListener.editShopCart(item.getGId(),item.getGName(),item.getSeId(),item.getSeName(),
                item.getAIds(),item.getANames(),item.getPirce(),"1",item.getImg()));
        //减少商品
        helper.getView(R.id.iv_less).setOnClickListener(v -> mListener.editShopCart(item.getGId(),item.getGName(),item.getSeId(),item.getSeName(),
                item.getAIds(),item.getANames(),item.getPirce(),"-1",item.getImg()));
    }

    public void setOnShoppingCartEditListener(OnShoppingCartEditListener listener) {
        mListener = listener;
    }

    public interface OnShoppingCartEditListener {

        void editShopCart(String gId, String gName, String seId, String seName,
                     String aIds, String aNames, String price, String num,String img);
    }
}
