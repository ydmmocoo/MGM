package com.fjx.mg.food.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.view.RoundImageView;
import com.fjx.mg.view.WrapContentGridView;
import com.fjx.mg.view.flowlayout.TagFlowLayout;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.DimensionUtil;
import com.library.repository.models.HomeShopListBean;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yedeman
 * @date 2020/6/4.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class RvMenuStoreListAdapter extends BaseQuickAdapter<HomeShopListBean.ShopListBean, BaseViewHolder> {

    public RvMenuStoreListAdapter(int layoutResId, @Nullable List<HomeShopListBean.ShopListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, HomeShopListBean.ShopListBean item) {
        //设置商品图片
        RoundImageView ivPic=helper.getView(R.id.iv_pic);
        ivPic.setRectAdius(DimensionUtil.dip2px(5));
        CommonImageLoader.load(item.getLogo()).error(R.drawable.big_image_default)
                .placeholder(R.drawable.big_image_default).into(ivPic);
        //设置店铺名
        helper.setText(R.id.tv_store_name,item.getName());
        //设置评分
        helper.setText(R.id.tv_score,item.getAvgScore());
        //设置月售
        helper.setText(R.id.tv_monthly_sales,getContext().getResources().getString(R.string.monthly_sales,
                String.valueOf(item.getSaleCount())));
        //设置tag
        String tag = getContext().getResources().getString(R.string.deliveries, item.getDeliveryPrice());
        if (item.getDistributionFee().equals("0")) {
            tag = tag.concat(" | ").concat(getContext().getResources().getString(R.string.free_delivery_fee));
        } else {
            tag = tag.concat(" | ").concat(getContext().getResources().getString(R.string.delivery_fee,
                    item.getDeliveryPrice()));
        }
        if (item.getIsTakeYouself().equals("1")) {
            tag = tag.concat(" | ").concat(getContext().getResources().getString(R.string.support_self_mention));
        } else {
            tag = tag.concat("");
        }
        helper.setText(R.id.tv_tag, tag);
        //设置优惠 fl_discount
        List<String> list = new ArrayList<>();
        for (int i = 0; i < item.getReductionList().size(); i++) {
            list.add(getContext().getResources().getString(R.string.full_reduction,
                    item.getReductionList().get(i).getFullPrice(),
                    item.getReductionList().get(i).getPrice()));
        }
        TagFlowLayout tagFlowLayout = helper.getView(R.id.fl_discount);
        TextTagAdapter adapter = new TextTagAdapter(getContext(), list);
        tagFlowLayout.setAdapter(adapter);
        //设置距离 tv_distance
        if (item.getDistance() > 1000) {
            DecimalFormat df = new DecimalFormat("#0.00");
            helper.setText(R.id.tv_distance, getContext().getResources().getString(R.string.distance_detail,
                    df.format(item.getDistance() / 1000).concat("k")));
        } else {
            helper.setText(R.id.tv_distance, getContext().getResources().getString(R.string.distance_detail,
                    String.valueOf(item.getDistance())));
        }
        //设置商品
        WrapContentGridView gv = helper.getView(R.id.gv_goods);
        GvGoodsAdapter gvGoodsAdapter = new GvGoodsAdapter(getContext(), item.getGoodsList());
        gv.setAdapter(gvGoodsAdapter);
    }
}
