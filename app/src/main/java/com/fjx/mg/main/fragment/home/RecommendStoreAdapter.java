package com.fjx.mg.main.fragment.home;

import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.fjx.mg.R;
import com.fjx.mg.food.adapter.TextTagAdapter;
import com.fjx.mg.view.RoundImageView;
import com.fjx.mg.view.flowlayout.TagFlowLayout;
import com.library.common.base.adapter.BaseAdapter;
import com.library.common.utils.CommonImageLoader;
import com.library.common.utils.DimensionUtil;
import com.library.repository.models.CollectShopsBean;
import com.library.repository.repository.RepositoryFactory;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RecommendStoreAdapter extends BaseAdapter<CollectShopsBean.ShopListBean> {

    public RecommendStoreAdapter() {
        super(R.layout.item_rv_store);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectShopsBean.ShopListBean item) {
        //设置店铺Logo iv_logo
        RoundImageView mIvPic = helper.getView(R.id.iv_logo);
        mIvPic.setRectAdius(DimensionUtil.dip2px(5));
        CommonImageLoader.load(item.getShopLogo())
                .placeholder(R.drawable.food_default).into(mIvPic);
        //设置店铺名 tv_store_name
        helper.setText(R.id.tv_store_name,item.getShopName());
        //设置评分 tv_score
        helper.setText(R.id.tv_score,item.getAvgScore());
        //设置月售 tv_monthly_sales
        helper.setText(R.id.tv_monthly_sales,getContext().getResources().getString(R.string.monthly_sales,
                String.valueOf(item.getSaleNum())));
        //设置标签 tv_tag
        String tag=getContext().getResources().getString(R.string.deliveries,item.getDeliveryPrice());
        if ("0".equals(item.getDistributionFee())){
            tag=tag.concat(" | ").concat(getContext().getResources().getString(R.string.free_delivery_fee));
        }else {
            tag=tag.concat(" | ").concat(getContext().getResources().getString(R.string.delivery_fee,
                    item.getDeliveryPrice()));
        }
        if (item.getIsTakeYouself().equals("1")){
            tag=tag.concat(" | ").concat(getContext().getResources().getString(R.string.support_self_mention));
        }else {
            tag=tag.concat("");
        }
        helper.setText(R.id.tv_tag,tag);
        //设置优惠 fl_discount
        List<String> list=new ArrayList<>();
        String language = RepositoryFactory.getLocalRepository().getLangugeType();
        int size=0;
        if (language.equals("zh-ch")||language.equals("zh-tw")) {
            size = Math.min(item.getReductionList().size(), 4);
        }else {
            size = Math.min(item.getReductionList().size(), 3);
        }
        for (int i=0;i<size;i++){
            list.add(getContext().getResources().getString(R.string.full_reduction,
                    item.getReductionList().get(i).getFullPrice(),
                    item.getReductionList().get(i).getPrice()));
        }
        TagFlowLayout tagFlowLayout=helper.getView(R.id.fl_discount);
        TextTagAdapter adapter=new TextTagAdapter(getContext(),list);
        tagFlowLayout.setAdapter(adapter);
        //设置距离 tv_distance
        helper.setText(R.id.tv_distance, item.getDistance().concat("km"));
    }
}
