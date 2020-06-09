package com.fjx.mg.nearbycity.adapter;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fjx.mg.R;
import com.fjx.mg.nearbycity.holder.HotShopViewHolder;
import com.fjx.mg.nearbycity.holder.NearbyCityViewHolder;
import com.fjx.mg.nearbycity.holder.TopViewHolder;
import com.library.common.utils.StringUtil;
import com.library.repository.models.NearbyCItyGetListModel;
import com.library.repository.models.NearbyCityConfigModel;
import com.library.repository.models.NearbyCityHotCompanyModel;

/**
 * Author    by Administrator
 * Date      on 2019/10/16.
 * Description：多类型同城适配器
 */
public class NearbyCityMultiTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 顶部分类列表
     */
    private static final int TOP = 0;

    /**
     * 热门分类列表
     */
    private static final int HOT_SHOP = 1;

    /**
     * 同城推荐列表
     */
    private static final int ALL_NEARBY_CITY = 2;

    /**
     * 当前类型
     */
    public int currentType = TOP;

    private int typeNumber = 3;

    private String mJson;
    private NearbyCityConfigModel mData;
    private NearbyCityHotCompanyModel mModel;
    private NearbyCItyGetListModel mNearbyCItyGetListModel;

    public NearbyCityMultiTypeAdapter(NearbyCityConfigModel data, NearbyCityHotCompanyModel model, NearbyCItyGetListModel model2, String json) {
        this.mData = data;
        this.mModel = model;
        mNearbyCItyGetListModel = model2;
        mJson = json;
    }

    public void setAllData(){

    }

    @Override
    public int getItemCount() {
        return typeNumber;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case TOP:
                currentType = TOP;
                break;
            case HOT_SHOP:
                currentType = HOT_SHOP;
                break;
            case ALL_NEARBY_CITY:
                currentType = ALL_NEARBY_CITY;
                break;
            default:
                break;
        }
        return currentType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (TOP == viewType) {
            return new TopViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nearby_city_top_parent_layout, null, false));
        } else if (HOT_SHOP == viewType) {
            return new HotShopViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nearby_city_hot_shop_parent_layout, null, false));
        } else if (ALL_NEARBY_CITY == viewType) {
            return new NearbyCityViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nearby_city_parent_layout, null, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (getItemViewType(position) == TOP) {
            TopViewHolder topViewHolder = (TopViewHolder) viewHolder;
            if (!mData.getTypeList().isEmpty()) {
                if (StringUtil.isEmpty(mJson)) {
                    topViewHolder.setData(mData.getTypeList(), mData);
                } else {
                    topViewHolder.setData(mData.getTypeList(), mData, mJson);
                }
            }
        } else if (getItemViewType(position) == HOT_SHOP) {
            HotShopViewHolder hotShopViewHolder = (HotShopViewHolder) viewHolder;
            if (!mModel.getCompanyList().isEmpty()) {
                hotShopViewHolder.setData(mModel.getCompanyList());
            }
        } else if (getItemViewType(position) == ALL_NEARBY_CITY) {
            NearbyCityViewHolder nearbyCityViewHolder = (NearbyCityViewHolder) viewHolder;
            if (mNearbyCItyGetListModel==null){
                nearbyCityViewHolder.setData(null);
            }else {
                nearbyCityViewHolder.setData(mNearbyCItyGetListModel.getCityCircleList());
            }
        }

    }
}
