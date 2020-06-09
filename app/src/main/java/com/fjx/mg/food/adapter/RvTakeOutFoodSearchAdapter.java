package com.fjx.mg.food.adapter;

import android.content.Intent;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.food.StoreListActivity;
import com.fjx.mg.food.model.SearchEntity;
import com.fjx.mg.view.RoundImageView;
import com.fjx.mg.view.WrapContentGridView;
import com.fjx.mg.view.flowlayout.TagFlowLayout;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.DimensionUtil;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * @author yedeman
 * @date 2020/5/19.
 * email：ydmmocoo@gmail.com
 * description：
 */
public class RvTakeOutFoodSearchAdapter extends BaseMultiItemQuickAdapter<SearchEntity, BaseViewHolder> {

    public RvTakeOutFoodSearchAdapter(List<SearchEntity> data) {
        super(data);
        addItemType(SearchEntity.TYPE_HEADER, R.layout.item_take_out_food_search_header);
        addItemType(SearchEntity.TYPE_TAKE_OUT_FOOD, R.layout.item_take_out_food_search_content);
        addItemType(SearchEntity.TYPE_FOOTER, R.layout.item_take_out_food_search_footer);
        addItemType(SearchEntity.TYPE_TO_THE_STORE, R.layout.item_take_out_food_search_to_the_store);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder helper, SearchEntity item) {
        switch (item.getItemType()) {
            case SearchEntity.TYPE_HEADER://头部
                helper.setText(R.id.tv_title, item.getTitle());
                break;
            case SearchEntity.TYPE_FOOTER://查看更多外卖
                helper.getView(R.id.tv_more).setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), StoreListActivity.class);
                    getContext().startActivity(intent);
                });
                break;
            case SearchEntity.TYPE_TAKE_OUT_FOOD://外卖
                //设置店铺Logo
                RoundImageView ivLogo = helper.getView(R.id.iv_pic);
                ivLogo.setRectAdius(DimensionUtil.dip2px(5));
                CommonImageLoader.load(item.getData().getLogo()).error(R.drawable.big_image_default)
                        .placeholder(R.drawable.big_image_default).into(ivLogo);
                //设置店铺名
                helper.setText(R.id.tv_store_name, item.getData().getName());
                //评分
                helper.setText(R.id.tv_score, item.getData().getAvgScore());
                //月销
                helper.setText(R.id.tv_monthly_sales, getContext().getResources().getString(R.string.monthly_sales, String.valueOf(item.getData().getSaleCount())));
                //设置tag
                String tag = getContext().getResources().getString(R.string.deliveries, item.getData().getDeliveryPrice());
                if (item.getData().getDistributionFee().equals("0")) {
                    tag = tag.concat(" | ").concat(getContext().getResources().getString(R.string.free_delivery_fee));
                } else {
                    tag = tag.concat(" | ").concat(getContext().getResources().getString(R.string.delivery_fee,
                            item.getData().getDeliveryPrice()));
                }
                if (item.getData().getIsTakeYouself().equals("1")) {
                    tag = tag.concat(" | ").concat(getContext().getResources().getString(R.string.support_self_mention));
                } else {
                    tag = tag.concat("");
                }
                helper.setText(R.id.tv_tag, tag);
                //设置优惠 fl_discount
                List<String> list = new ArrayList<>();
                for (int i = 0; i < item.getData().getReductionList().size(); i++) {
                    list.add(getContext().getResources().getString(R.string.full_reduction,
                            item.getData().getReductionList().get(i).getFullPrice(),
                            item.getData().getReductionList().get(i).getPrice()));
                }
                TagFlowLayout tagFlowLayout = helper.getView(R.id.fl_discount);
                TextTagAdapter adapter = new TextTagAdapter(getContext(), list);
                tagFlowLayout.setAdapter(adapter);
                //设置距离 tv_distance
                if (item.getData().getDistance() > 1000) {
                    DecimalFormat df = new DecimalFormat("#0.00");
                    helper.setText(R.id.tv_distance, getContext().getResources().getString(R.string.distance_detail,
                            df.format(item.getData().getDistance() / 1000).concat("k")));
                } else {
                    helper.setText(R.id.tv_distance, getContext().getResources().getString(R.string.distance_detail,
                            String.valueOf(item.getData().getDistance())));
                }
                //设置商品
                WrapContentGridView gv = helper.getView(R.id.gv_goods);
                GvGoodsAdapter gvGoodsAdapter = new GvGoodsAdapter(getContext(), item.getData().getGoodsList());
                gv.setAdapter(gvGoodsAdapter);
                break;
            case SearchEntity.TYPE_TO_THE_STORE://到店
                break;
        }
    }
}
